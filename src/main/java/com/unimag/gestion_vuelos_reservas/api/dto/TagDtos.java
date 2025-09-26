package com.unimag.gestion_vuelos_reservas.api.dto;

import java.io.Serializable;
import java.util.Set;

public class TagDtos {
    public record TagCreateRequest( String name) implements Serializable {}
    public record TagUpdateRequest(Long id,String name) implements Serializable {}
    public record TagResponse(Long id, String name, Set<FlightDtos.FlightResponse> fligh) implements Serializable {}
}
