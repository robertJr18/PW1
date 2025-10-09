package com.unimag.gestion_vuelos_reservas.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

public class AirlineDtos {
    public record AirlineCreateRequest(@NotBlank @Size(min=2 , max=3) String code, @NotBlank String name) implements Serializable {}
    public record AirlineUpdateRequest(String code, String name) implements Serializable {}
    public record AirlineResponse(Long id, String code, String name) implements Serializable {}
    public record AirlineDetailResponse(Long id, String code, String name, List<FlightDtos.FlightResponse> flights) implements Serializable {}
}

