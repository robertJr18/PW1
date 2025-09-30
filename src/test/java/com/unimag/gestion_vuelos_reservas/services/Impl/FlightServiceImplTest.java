package com.unimag.gestion_vuelos_reservas.services.Impl;

import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.AirlineRepository;
import com.unimag.gestion_vuelos_reservas.repositories.AirportRepository;
import com.unimag.gestion_vuelos_reservas.repositories.FlightRepository;
import com.unimag.gestion_vuelos_reservas.repositories.TagRepository;
import com.unimag.gestion_vuelos_reservas.services.impl.FlightServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    @Test
    void shouldCreateFlight() {
        var airline = Airline.builder().id(1L).code("AV").name("Avianca").build();
        var origin = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();
        var destination = Airport.builder().id(2L).code("MDE").name("Olaya Herrera").city("Medellín").build();
        var tag = Tag.builder().id(1L).name("promo").build();

        var request = new FlightDtos.FlightCreateRequest(
                "AV123",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(1),
                1L,
                1L,
                2L,
                Set.of(1L)
        );

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airportRepository.findById(1L)).thenReturn(Optional.of(origin));
        when(airportRepository.findById(2L)).thenReturn(Optional.of(destination));
        when(tagRepository.findAllById(Set.of(1L))).thenReturn(List.of(tag));
        when(flightRepository.save(any(Flight.class))).thenAnswer(inv -> {
            Flight f = inv.getArgument(0);
            f.setId(1L);
            return f;
        });

        var result = flightService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.number()).isEqualTo("AV123");

        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    void shouldThrowExceptionWhenAirlineNotFoundOnCreate() {
        var request = new FlightDtos.FlightCreateRequest(
                "AV123",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(1),
                999L,
                1L,
                2L,
                Set.of()
        );

        when(airlineRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airline not found");

        verify(airlineRepository).findById(999L);
        verify(flightRepository, never()).save(any());
    }

    @Test
    void shouldGetFlightById() {
        var airline = Airline.builder().id(1L).code("AV").name("Avianca").build();
        var origin = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();
        var destination = Airport.builder().id(2L).code("MDE").name("Olaya Herrera").city("Medellín").build();

        var entity = Flight.builder()
                .id(1L)
                .number("AV123")
                .airline(airline)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();

        when(flightRepository.findById(1L)).thenReturn(Optional.of(entity));

        var result = flightService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.number()).isEqualTo("AV123");

        verify(flightRepository).findById(1L);
    }

    @Test
    void shouldUpdateFlight() {
        var airline = Airline.builder().id(1L).code("AV").name("Avianca").build();
        var origin = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();
        var destination = Airport.builder().id(2L).code("MDE").name("Olaya Herrera").city("Medellín").build();

        var entity = Flight.builder()
                .id(1L)
                .number("AV123")
                .airline(airline)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();

        var request = new FlightDtos.FlightUpdateRequest(
                "AV124",
                OffsetDateTime.now().plusDays(1),
                OffsetDateTime.now().plusDays(1).plusHours(1),
                1L,
                1L,
                2L,
                Set.of()
        );

        when(flightRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tagRepository.findAllById(Set.of())).thenReturn(List.of());
        when(flightRepository.save(any(Flight.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = flightService.update(1L, request);

        assertThat(result).isNotNull();
        assertThat(result.number()).isEqualTo("AV124");

        verify(flightRepository).findById(1L);
        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    void shouldDeleteFlight() {
        when(flightRepository.existsById(1L)).thenReturn(true);

        flightService.delete(1L);

        verify(flightRepository).existsById(1L);
        verify(flightRepository).deleteById(1L);
    }

    @Test
    void shouldSearchFlightsByOriginAndDestination() {
        // given
        var airline = Airline.builder().id(1L).code("AV").name("Avianca").build();
        var origin = Airport.builder().id(1L).code("bog").name("El Dorado").city("Bogotá").build();
        var destination = Airport.builder().id(2L).code("mde").name("Olaya Herrera").city("Medellín").build();

        var flight = Flight.builder()
                .id(1L)
                .number("AV123")
                .airline(airline)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();

        var from = OffsetDateTime.now().minusDays(1);
        var to   = OffsetDateTime.now().plusDays(1);
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);

        // Stub del repositorio PARA EL MÉTODO PAGINADO por códigos (ajusta el nombre si el tuyo difiere)
        when(flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
                eq("bog"), eq("mde"), eq(from), eq(to), eq(pageable)
        )).thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of(flight), pageable, 1));

        // when (nota: pasamos en minúscula para probar normalización interna del service)
        var page = flightService.searchFlights("bog", "mde", from, to, pageable);

        // then
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        var result = page.getContent().get(0);
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.number()).isEqualTo("AV123");
        assertThat(result.origin().code()).isEqualTo("bog");
        assertThat(result.destination().code()).isEqualTo("mde");
        verifyNoMoreInteractions(flightRepository);
    }
}
