package com.unimag.gestion_vuelos_reservas.services.Impl;

import com.unimag.gestion_vuelos_reservas.api.dto.SeatInventoryDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.FlightRepository;
import com.unimag.gestion_vuelos_reservas.repositories.SeatInventoryRepository;
import com.unimag.gestion_vuelos_reservas.services.impl.SeatInventoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatInventoryServiceImplTest {

    @Mock
    private SeatInventoryRepository seatInventoryRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private SeatInventoryServiceImpl seatInventoryService;

    @Test
    void shouldCreateSeatInventory() {
        var airline = Airline.builder().id(1L).code("AV").name("Avianca").build();
        var origin = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();
        var destination = Airport.builder().id(2L).code("MDE").name("Olaya Herrera").city("Medellín").build();

        var flight = Flight.builder()
                .id(1L)
                .number("AV123")
                .airline(airline)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();

        var request = new SeatInventoryDtos.SeatInventoryCreateRequest(
                Cabin.ECONOMY,
                100,
                100,
                1L
        );

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatInventoryRepository.findByFlight_IdAndCabin(1L, Cabin.ECONOMY))
                .thenReturn(Optional.empty());
        when(seatInventoryRepository.save(any(SeatInventory.class))).thenAnswer(inv -> {
            SeatInventory s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        var result = seatInventoryService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.cabin()).isEqualTo(Cabin.ECONOMY);
        assertThat(result.totalSeats()).isEqualTo(100);
        assertThat(result.availableSeats()).isEqualTo(100);

        verify(seatInventoryRepository).save(any(SeatInventory.class));
    }

    @Test
    void shouldThrowExceptionWhenFlightNotFoundOnCreate() {
        var request = new SeatInventoryDtos.SeatInventoryCreateRequest(
                Cabin.ECONOMY, 100, 100, 999L
        );

        when(flightRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> seatInventoryService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Flight not found");

        verify(flightRepository).findById(999L);
        verify(seatInventoryRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateCabinOnCreate() {
        var flight = Flight.builder().id(1L).number("AV123").build();
        var existing = SeatInventory.builder()
                .id(1L)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .totalSeats(100)
                .availableSeats(100)
                .build();

        var request = new SeatInventoryDtos.SeatInventoryCreateRequest(
                Cabin.ECONOMY, 100, 100, 1L
        );

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatInventoryRepository.findByFlight_IdAndCabin(1L, Cabin.ECONOMY))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> seatInventoryService.create(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already exists");

        verify(seatInventoryRepository, never()).save(any());
    }

    @Test
    void shouldGetSeatInventoryById() {
        var airline = Airline.builder().id(1L).code("AV").name("Avianca").build();
        var origin = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();
        var destination = Airport.builder().id(2L).code("MDE").name("Olaya Herrera").city("Medellín").build();

        var flight = Flight.builder()
                .id(1L)
                .number("AV123")
                .airline(airline)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();

        var entity = SeatInventory.builder()
                .id(1L)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .totalSeats(100)
                .availableSeats(80)
                .build();

        when(seatInventoryRepository.findById(1L)).thenReturn(Optional.of(entity));

        var result = seatInventoryService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.availableSeats()).isEqualTo(80);

        verify(seatInventoryRepository).findById(1L);
    }

    @Test
    void shouldUpdateSeatInventory() {
        var flight = Flight.builder().id(1L).number("AV123").build();
        var entity = SeatInventory.builder()
                .id(1L)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .totalSeats(100)
                .availableSeats(100)
                .build();

        var request = new SeatInventoryDtos.SeatInventoryUpdateRequest(
                Cabin.ECONOMY, 120, 120, 1L
        );

        when(seatInventoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatInventoryRepository.save(any(SeatInventory.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = seatInventoryService.update(1L, request);

        assertThat(result).isNotNull();
        assertThat(result.totalSeats()).isEqualTo(120);

        verify(seatInventoryRepository).findById(1L);
        verify(seatInventoryRepository).save(any(SeatInventory.class));
    }

    @Test
    void shouldDeleteSeatInventory() {
        when(seatInventoryRepository.existsById(1L)).thenReturn(true);

        seatInventoryService.delete(1L);

        verify(seatInventoryRepository).existsById(1L);
        verify(seatInventoryRepository).deleteById(1L);
    }

    @Test
    void shouldGetInventoriesByFlight() {
        var airline = Airline.builder().id(1L).code("AV").name("Avianca").build();
        var origin = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();
        var destination = Airport.builder().id(2L).code("MDE").name("Olaya Herrera").city("Medellín").build();

        var flight = Flight.builder()
                .id(1L)
                .number("AV123")
                .airline(airline)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();

        var inventory1 = SeatInventory.builder()
                .id(1L)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .totalSeats(100)
                .availableSeats(80)
                .build();

        var inventory2 = SeatInventory.builder()
                .id(2L)
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .totalSeats(20)
                .availableSeats(15)
                .build();

        when(seatInventoryRepository.findByFlight_Id(1L))
                .thenReturn(List.of(inventory1, inventory2));

        var result = seatInventoryService.getInventoriesByFlight(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).cabin()).isEqualTo(Cabin.ECONOMY);
        assertThat(result.get(0).totalSeats()).isEqualTo(100);
        assertThat(result.get(0).availableSeats()).isEqualTo(80);
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).cabin()).isEqualTo(Cabin.BUSINESS);
        assertThat(result.get(1).totalSeats()).isEqualTo(20);
        assertThat(result.get(1).availableSeats()).isEqualTo(15);

        verify(seatInventoryRepository).findByFlight_Id(1L);
    }
}
