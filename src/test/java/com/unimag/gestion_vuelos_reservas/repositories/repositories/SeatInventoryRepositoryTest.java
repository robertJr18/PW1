package com.unimag.gestion_vuelos_reservas.repositories.repositories;

import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.AirlineRepository;
import com.unimag.gestion_vuelos_reservas.repositories.AirportRepository;
import com.unimag.gestion_vuelos_reservas.repositories.FlightRepository;
import com.unimag.gestion_vuelos_reservas.repositories.SeatInventoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

public class SeatInventoryRepositoryTest  extends AbstractRepositoryTI {

    @Autowired
    private SeatInventoryRepository seatInventoryRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private AirlineRepository airlineRepository;

    @Test
    @Transactional
    @DisplayName("encuntra vuelo por id y su cabina")
    void findByFlight_IdAndCabin(){
        //given
        var flight = Flight.builder().number("F100")
                .origin(airportRepository.save(Airport.builder().name("dorado").build()))
                .destination(airportRepository.save(Airport.builder().name("nevado").build()))
                .airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAO").build())).build();
        flightRepository.save(flight);

        var seatInventory  = SeatInventory.builder().flight(flight).cabin(Cabin.BUSINESS).availableSeats(8).totalSeats(240).build();
        seatInventoryRepository.save(seatInventory);

        //when

        Optional<SeatInventory> result = seatInventoryRepository.findByFlight_IdAndCabin(flight.getId(),Cabin.BUSINESS);

        //then

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getFlight().getNumber()).isEqualTo("F100");
        assertThat(result.get().getCabin()).isEqualTo(Cabin.BUSINESS);

    }
    @Test
    @DisplayName("verifica si hay suficientes asientsos")
    void hasAvailableSeats(){
        //given
        var flight1 = Flight.builder().number("F1001")
                .origin(airportRepository.save(Airport.builder().name("dorado").build()))
                .destination(airportRepository.save(Airport.builder().name("nevado").build()))
                .airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAOO").build())).build();
        flightRepository.saveAndFlush(flight1);

        var seatInventory  = SeatInventory.builder().flight(flight1).cabin(Cabin.BUSINESS).availableSeats(8).totalSeats(240).build();
        seatInventoryRepository.save(seatInventory);

        //when
        boolean enoughSeats = seatInventoryRepository.hasAvailableSeats(flight1.getId(),Cabin.BUSINESS,3);

        //then
        assertThat(enoughSeats).isTrue();
        assertThat(seatInventory.getAvailableSeats()).isEqualTo(8);
    }


}
