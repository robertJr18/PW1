package com.unimag.gestion_vuelos_reservas.api.dto;

import com.unimag.gestion_vuelos_reservas.models.Airport;

import java.io.Serializable;
import java.util.List;

public class AirportDtos {
    public record AirportCreateRequest(String code, String name, String city) implements Serializable {}
    public record AirportUpdateRequest(String code, String name, String city) implements Serializable {}
    public record AirportResponse(Long id,String code, String name, String city) implements Serializable {}
}

