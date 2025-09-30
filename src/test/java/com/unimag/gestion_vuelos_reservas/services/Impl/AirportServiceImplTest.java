package com.unimag.gestion_vuelos_reservas.services.Impl;

import com.unimag.gestion_vuelos_reservas.api.dto.AirportDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.repositories.AirportRepository;
import com.unimag.gestion_vuelos_reservas.services.impl.AirportServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AirportServiceImplTest {
    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportServiceImpl airportService;

    @Test
    void shouldCreateAirport() {
        var request = new AirportDtos.AirportCreateRequest("bog", "El Dorado", "Bogotá");
        var savedEntity = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();

        when(airportRepository.findByCode("BOG")).thenReturn(Optional.empty());
        when(airportRepository.save(any(Airport.class))).thenReturn(savedEntity);

        var result = airportService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.code()).isEqualTo("BOG");
        assertThat(result.name()).isEqualTo("El Dorado");
        assertThat(result.city()).isEqualTo("Bogotá");

        verify(airportRepository).findByCode("BOG");
        verify(airportRepository).save(any(Airport.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateCode() {
        var request = new AirportDtos.AirportCreateRequest("bog", "El Dorado", "Bogotá");
        var existing = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();

        when(airportRepository.findByCode("BOG")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> airportService.create(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Airport code already exists");

        verify(airportRepository).findByCode("BOG");
        verify(airportRepository, never()).save(any());
    }

    @Test
    void shouldGetAirportById() {
        var entity = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();

        when(airportRepository.findById(1L)).thenReturn(Optional.of(entity));

        var result = airportService.get(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.code()).isEqualTo("BOG");
        assertThat(result.city()).isEqualTo("Bogotá");

        verify(airportRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAirportNotFound() {
        when(airportRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> airportService.get(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airport not found");

        verify(airportRepository).findById(999L);
    }

    @Test
    void shouldGetAirportByCode() {
        var entity = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();

        when(airportRepository.findByCode("BOG")).thenReturn(Optional.of(entity));

        var result = airportService.getByCode("bog");

        assertThat(result).isNotNull();
        assertThat(result.code()).isEqualTo("BOG");

        verify(airportRepository).findByCode("BOG");
    }

    @Test
    void shouldUpdateAirport() {
        var entity = Airport.builder().id(1L).code("BOG").name("El Dorado").city("Bogotá").build();
        var request = new AirportDtos.AirportUpdateRequest("BOG", "El Dorado International", "Bogotá D.C.");

        when(airportRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(airportRepository.save(any(Airport.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = airportService.update(1L, request);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("El Dorado International");
        assertThat(result.city()).isEqualTo("Bogotá D.C.");

        verify(airportRepository).findById(1L);
        verify(airportRepository).save(any(Airport.class));
    }

    @Test
    void shouldDeleteAirport() {
        when(airportRepository.existsById(1L)).thenReturn(true);

        airportService.delete(1L);

        verify(airportRepository).existsById(1L);
        verify(airportRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentAirport() {
        when(airportRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> airportService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airport not found");

        verify(airportRepository).existsById(999L);
        verify(airportRepository, never()).deleteById(any());
    }
}
