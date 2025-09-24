package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class BookingRepositoryTest extends AbstractRepositoryTI {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingItemRepository bookingItemRepository;

    @Test
    @DisplayName("Booking: encuentra reservas de un pasajero paginadas y ordenadas por fecha desc")
    void shouldFindBookingsByPassengerEmailOrdered() {
        // GIVEN
        // 1. Crear Passenger
        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("test@demo.com")
                        .fullName("Test User")
                        .profile(PassengerProfile.builder().phone("11111").CountryCode("57").build())
                        .build()
        );


        var Booking1 = Booking.builder()
                .createdAt(OffsetDateTime.now().minusDays(10))
                .passenger(passenger)
                .build();
        bookingRepository.save(Booking1);


        var Booking2 = Booking.builder()
                .createdAt(OffsetDateTime.now())
                .passenger(passenger)
                .build();
        bookingRepository.save(Booking2);

        // WHEN
        Page<Booking> result = bookingRepository
                .findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc("TEST@DEMO.COM", PageRequest.of(0, 10));

        // THEN
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(Booking1.getId());
        assertThat(result.getContent().get(1).getId()).isEqualTo(Booking2.getId());
    }

    void findByIdWithItemsAndFlightsAndPassenger(){

        //given
        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("sum@test.com")
                        .fullName("Jose r")
                        .profile(PassengerProfile.builder().phone("300554").CountryCode("57").build())
                        .build()
        );

        var booking = bookingRepository.save(
                Booking.builder()
                        .createdAt(OffsetDateTime.now())
                        .passenger(passenger)
                        .build()
        );

        var flight1 = Flight.builder().number("F300").origin(Airport.builder().name("dorado").build()).destination(Airport.builder().name("nevado").build()).build();
        flightRepository.save(flight1);
        var flight2 = Flight.builder().number("FS400").origin(Airport.builder().name("nevado").build()).destination(Airport.builder().name("dorado").build()).build();
        flightRepository.save(flight2);



        var item1 = BookingItem.builder().segmentOrder(1).cabin(Cabin.BUSINESS).price(new BigDecimal("400")).flight(flight1).booking(booking).build();
        bookingItemRepository.save(item1);
        var item2 = BookingItem.builder().segmentOrder(2).cabin(Cabin.BUSINESS).price(new BigDecimal("450")).flight(flight2).booking(booking).build();
        bookingItemRepository.save(item2);


        booking.setItems(List.of(item1,item2));

        bookingRepository.saveAndFlush(booking);

        //when

        Optional<Booking> result = bookingRepository.findByIdWithItemsAndFlightsAndPassenger(booking.getId());

        //then

        assertThat(result.isPresent()).isTrue();

        var loadedBooking = result.get();

        //pasajero precargado

        assertThat(loadedBooking.getPassenger().getEmail()).isEqualTo("sum@test.com");
        assertThat(loadedBooking.getPassenger().getFullName()).isEqualTo("Jose r");

        // items precargado
        assertThat(loadedBooking.getItems()).hasSize(2);

        // Flights precargado

        assertThat(loadedBooking.getItems().get(0).getFlight().getNumber()).isEqualTo("F300");
        assertThat(loadedBooking.getItems().get(1).getFlight().getNumber()).isEqualTo("FS400");

    }
}
