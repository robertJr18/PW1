package com.unimag.gestion_vuelos_reservas.api.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class TagDtos {
    public record TagCreateRequest( String name) implements Serializable {}
    public record TagUpdateRequest(String name) implements Serializable {}
    public record TagResponse(Long id, String name, Collection<FlightDtos.FlightResponse> flights) implements Serializable {}
}
