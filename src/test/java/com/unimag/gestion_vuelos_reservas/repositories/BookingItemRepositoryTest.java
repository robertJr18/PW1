package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.*;
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
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private PassengerProfileRepository passengerProfileRepository;

    @Test
    @DisplayName("BookingItem: lista segmentos ordenados por segmentOrder")
    void shouldListItemsOrderedBySegmentOrder() {
        // GIVEN
        var profile= PassengerProfile.builder() .phone("123456789").countryCode("57") .build();
        passengerProfileRepository.save(profile);

        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("seg@test.com")
                        .fullName("Seg Tester")
                        .profile(profile)
                        .build()
        );

        var booking = bookingRepository.save(
                Booking.builder()
                        .createdAt(OffsetDateTime.now())
                        .passenger(passenger)
                        .build()
        );

        var flight1 = Flight.builder().number("F100").
                origin(airportRepository.save(Airport.builder().name("dorado").build()))
                .destination(airportRepository.save(Airport.builder().name("nevado").build()))
                .airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAO").build())).build();
        flightRepository.saveAndFlush(flight1);

        var flight2 = Flight.builder().number("F200")
                .origin(airportRepository.save(Airport.builder().name("josht").build()))
                .destination(airportRepository.save(Airport.builder().name("airFonseca").build()))
                .airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAE").build())).build();
        flightRepository.saveAndFlush(flight2);


        var item2 = bookingItemRepository.saveAndFlush(
                BookingItem.builder()
                        .segmentOrder(2)
                        .cabin(Cabin.BUSINESS)
                        .price(new BigDecimal("400"))
                        .booking(booking)
                        .flight(flight2)
                        .build()
        );

        var item1 = bookingItemRepository.saveAndFlush(
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
        var pofile= PassengerProfile.builder() .phone("123456789").countryCode("57") .build();
        passengerProfileRepository.save(pofile);
        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("sum@test.com")
                        .fullName("Sum Tester")
                        .profile(pofile)
                        .build()
        );

        var booking = bookingRepository.save(
                Booking.builder()
                        .createdAt(OffsetDateTime.now())
                        .passenger(passenger)
                        .build()
        );

        var flight = Flight.builder().number("F300")
                .origin(airportRepository.save(Airport.builder().name("dorado").build()))
                .destination(airportRepository.save(Airport.builder().name("nevado").build()))
                .airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AA").build())).build();
        flightRepository.saveAndFlush(flight);


        bookingItemRepository.saveAll(List.of(
                BookingItem.builder().segmentOrder(1).cabin(Cabin.ECONOMY).price(new BigDecimal("150"))
                        .booking(booking)
                        .flight(flight).build(),
                BookingItem.builder().segmentOrder(2).cabin(Cabin.BUSINESS).price(new BigDecimal("350"))
                        .booking(booking)
                        .flight(flight).build()
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
        var pofile= PassengerProfile.builder() .phone("12345679").countryCode("57") .build();
        passengerProfileRepository.save(pofile);

        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("count@test.com")
                        .fullName("Count Tester")
                        .profile(pofile)
                        .build()
        );

        var booking = bookingRepository.save(
                Booking.builder()
                        .createdAt(OffsetDateTime.now())
                        .passenger(passenger)
                        .build()
        );

        var flight = Flight.builder().number("F40")
                .origin(airportRepository.save(Airport.builder().name("dorado").build()))
                .destination(airportRepository.save(Airport.builder().name("nevado").build()))
                .airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAAA").build())).build();
        flightRepository.saveAndFlush(flight);

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
