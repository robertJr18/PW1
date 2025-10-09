package com.unimag.gestion_vuelos_reservas.api.dto;

import com.unimag.gestion_vuelos_reservas.models.Airport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

public class AirportDtos {
    public record AirportCreateRequest(@NotBlank @Size(min= 3, max= 3) String code, @NotBlank String name, @NotBlank String city) implements Serializable {}
    public record AirportUpdateRequest(String code, String name, String city) implements Serializable {}
    public record AirportResponse(Long id,String code, String name, String city) implements Serializable {}
}

