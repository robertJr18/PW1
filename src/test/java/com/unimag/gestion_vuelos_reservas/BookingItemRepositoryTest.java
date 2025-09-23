package com.unimag.gestion_vuelos_reservas;

import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.BookingItemRepository;
import com.unimag.gestion_vuelos_reservas.repositories.BookingRepository;
import com.unimag.gestion_vuelos_reservas.repositories.PassengerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class BookingItemRepositoryTest extends AbstractRepositoryTI{

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private BookingItemRepository bookingItemRepository;

    @Test
    @DisplayName("BookingItem: lista segmentos ordenados por segmentOrder")
    void shouldListItemsOrderedBySegmentOrder() {
        // GIVEN
        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("seg@test.com")
                        .fullName("Seg Tester")
                        .profile(PassengerProfile.builder().phone("111").CountryCode("57").build())
                        .build()
        );

        var booking = bookingRepository.save(
                Booking.builder()
                        .createdAt(OffsetDateTime.now())
                        .passenger(passenger)
                        .build()
        );

        var flight1 = Flight.builder().number("F100").origin(Airport.builder().name("dorado").build()).destination(Airport.builder().name("nevado").build()).build();
        var flight2 = Flight.builder().number("F200").origin(Airport.builder().name("josht").build()).destination(Airport.builder().name("airFonseca").build()).build();

        var item2 = bookingItemRepository.save(
                BookingItem.builder()
                        .segmentOrder(2)
                        .cabin(Cabin.BUSINESS)
                        .price(new BigDecimal("400"))
                        .booking(booking)
                        .flight(flight2)
                        .build()
        );

        var item1 = bookingItemRepository.save(
                BookingItem.builder()
                        .segmentOrder(1)
                        .cabin(Cabin.ECONOMY)
                        .price(new BigDecimal("200"))
                        .booking(booking)
                        .flight(flight1)
                        .build()
        );

        // WHEN
        List<BookingItem> items = bookingItemRepository.findByBookingIdOrderBySegmentOrderAsc(booking.getId());

        // THEN
        assertThat(items).hasSize(2);
        assertThat(items.get(0).getSegmentOrder()).isEqualTo(1);
        assertThat(items.get(1).getSegmentOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("BookingItem: suma precios de los items de una reserva")
    void shouldSumPriceByBookingId() {
        // GIVEN
        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("sum@test.com")
                        .fullName("Sum Tester")
                        .profile(PassengerProfile.builder().phone("222").CountryCode("57").build())
                        .build()
        );

        var booking = bookingRepository.save(
                Booking.builder()
                        .createdAt(OffsetDateTime.now())
                        .passenger(passenger)
                        .build()
        );

        var flight = Flight.builder().number("F300").origin(Airport.builder().name("dorado").build()).destination(Airport.builder().name("nevado").build()).build();

        bookingItemRepository.saveAll(List.of(
                BookingItem.builder().segmentOrder(1).cabin(Cabin.ECONOMY).price(new BigDecimal("150")).booking(booking).flight(flight).build(),
                BookingItem.builder().segmentOrder(2).cabin(Cabin.BUSINESS).price(new BigDecimal("350")).booking(booking).flight(flight).build()
        ));

        // WHEN
        BigDecimal total = bookingItemRepository.sumPriceByBookingId(booking.getId());

        // THEN
        assertThat(total).isEqualByComparingTo("500");
    }

    @Test
    @DisplayName("BookingItem: cuenta asientos por vuelo y cabina")
    void shouldCountSeatsByFlightAndCabin() {
        // GIVEN
        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("count@test.com")
                        .fullName("Count Tester")
                        .profile(PassengerProfile.builder().phone("333").CountryCode("57").build())
                        .build()
        );

        var booking = bookingRepository.save(
                Booking.builder()
                        .createdAt(OffsetDateTime.now())
                        .passenger(passenger)
                        .build()
        );

        var flight = Flight.builder().number("F400").origin(Airport.builder().name("dorado").build()).destination(Airport.builder().name("nevado").build()).build();

        bookingItemRepository.saveAll(List.of(
                BookingItem.builder().segmentOrder(1).cabin(Cabin.ECONOMY).price(new BigDecimal("200")).booking(booking).flight(flight).build(),
                BookingItem.builder().segmentOrder(2).cabin(Cabin.ECONOMY).price(new BigDecimal("220")).booking(booking).flight(flight).build(),
                BookingItem.builder().segmentOrder(3).cabin(Cabin.BUSINESS).price(new BigDecimal("500")).booking(booking).flight(flight).build()
        ));

        // WHEN
        long economyCount = bookingItemRepository.countByFlightIdAndCabin(flight.getId(), Cabin.ECONOMY);
        long businessCount = bookingItemRepository.countByFlightIdAndCabin(flight.getId(), Cabin.BUSINESS);

        // THEN
        assertThat(economyCount).isEqualTo(2);
        assertThat(businessCount).isEqualTo(1);
    }
}
