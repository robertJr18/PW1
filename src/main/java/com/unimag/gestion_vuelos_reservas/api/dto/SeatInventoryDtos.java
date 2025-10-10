package com.unimag.gestion_vuelos_reservas.api.dto;

import com.unimag.gestion_vuelos_reservas.models.Cabin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class SeatInventoryDtos {
    public record SeatInventoryCreateRequest( Cabin cabin, @Min(1) Integer totalSeats, @Min(0) Integer availableSeats, @NotNull Long flightId) implements Serializable {}
    public record SeatInventoryUpdateRequest(Cabin cabin, Integer totalSeats, Integer availableSeats, Long flightId) implements Serializable {}
    public record SeatInventoryResponse(Long id, Cabin cabin, Integer totalSeats, Integer availableSeats, FlightDtos.FlightResponse flight) implements Serializable {}
}
