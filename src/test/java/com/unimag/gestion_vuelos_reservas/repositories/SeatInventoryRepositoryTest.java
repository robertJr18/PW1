package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Cabin;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.SeatInventory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

public class SeatInventoryRepositoryTest  extends AbstractRepositoryTI {
    @A
    private SeatInventoryRepository seatInventoryRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    @Transactional
    @DisplayName("encuntra vuelo por id y su cabina")
    void findByFlight_IdAndCabin(){
        //given
        var flight = Flight.builder().number("F300").origin(Airport.builder().name("dorado").build()).destination(Airport.builder().name("nevado").build()).build();
        flightRepository.save(flight);

        var seatInventory  = SeatInventory.builder().flight(flight).cabin(Cabin.BUSINESS).availableSeats(8).totalSeats(240).build();
        seatInventoryRepository.save(seatInventory);

        //when

        Optional<SeatInventory> result = seatInventoryRepository.findByFlight_IdAndCabin(flight.getId(),Cabin.BUSINESS);

        //then

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getFlight().getNumber()).isEqualTo("F300");
        assertThat(result.get().getCabin()).isEqualTo(Cabin.BUSINESS);

    }
    @Test
    @DisplayName("verifica si hay suficientes asientsos")
    void hasAvailableSeats(){
        //given
        var flight = Flight.builder().number("F300").origin(Airport.builder().name("dorado").build()).destination(Airport.builder().name("nevado").build()).build();
        flightRepository.save(flight);

        var seatInventory  = SeatInventory.builder().flight(flight).cabin(Cabin.BUSINESS).availableSeats(8).totalSeats(240).build();
        seatInventoryRepository.save(seatInventory);

        //when
        boolean enoughSeats = seatInventoryRepository.hasAvailableSeats(flight.getId(),Cabin.BUSINESS,3);

        //then
        assertThat(enoughSeats).isTrue();
        assertThat(seatInventory.getAvailableSeats()).isEqualTo(8);
    }


}
