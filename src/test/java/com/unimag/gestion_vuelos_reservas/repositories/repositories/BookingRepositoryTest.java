package com.unimag.gestion_vuelos_reservas.repositories.repositories;

import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryTest extends AbstractRepositoryTI {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private PassengerProfileRepository passengerProfileRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private AirlineRepository airlineRepository;
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
                        .profile(passengerProfileRepository.save(PassengerProfile.builder().phone("11111").countryCode("57").build()))
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
                .findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc("TEST@DEMO.COM", PageRequest.ofSize(10));

        // THEN
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void searchWithAllDetails(){

        //given
        var passenger = passengerRepository.save(
                Passenger.builder()
                        .email("sum@test.com")
                        .fullName("Jose r")
                        .profile(passengerProfileRepository.save(PassengerProfile.builder().phone("3005564").countryCode("57").build()))
                        .build()
        );

        var booking = bookingRepository.save(
                Booking.builder()
                        .createdAt(OffsetDateTime.now())
                        .passenger(passenger)
                        .build()
        );


        var flight1 = Flight.builder().number("F1001")
                .origin(airportRepository.save(Airport.builder().name("dorado").build()))
                .destination(airportRepository.save(Airport.builder().name("nevado").build()))
                .airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAU").build())).build();
        flightRepository.save(flight1);
        var flight2 = Flight.builder().number("F2001")
                .origin(airportRepository.save(Airport.builder().name("josht").build()))
                .destination(airportRepository.save(Airport.builder().name("airFonseca").build()))
                .airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAI").build())).build();
        flightRepository.save(flight2);




        var item1 = BookingItem.builder().segmentOrder(1).cabin(Cabin.BUSINESS).price(new BigDecimal("400"))
                .flight(flight1).booking(booking).build();
        bookingItemRepository.save(item1);
        var item2 = BookingItem.builder().segmentOrder(2).cabin(Cabin.BUSINESS).price(new BigDecimal("450"))
                .flight(flight2).booking(booking).build();
        bookingItemRepository.save(item2);


        booking.setItems(new ArrayList<>(List.of(item1, item2)));
        bookingRepository.save(booking);

        //when

        var result = bookingRepository.searchWithAllDetails(booking.getId());

        // then
        assertThat(result).isPresent();
        var bookingLoaded = result.get();

        assertThat(bookingLoaded.getPassenger())
                .isNotNull()
                .extracting(Passenger::getEmail)
                .isEqualTo("sum@test.com");

        assertThat(bookingLoaded.getItems())
                .hasSize(2)
                .extracting(BookingItem::getFlight)
                .extracting(Flight::getNumber)
                .containsExactlyInAnyOrder("F1001", "F2001");

    }
}
