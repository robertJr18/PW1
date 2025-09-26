package com.unimag.gestion_vuelos_reservas.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public class FlightDtos {
    public record FlightCreateRequest(String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                      AirlineDtos.AirlineCreateRequest airline,
                                      AirportDtos.AirportCreateRequest origin, AirportDtos.AirportCreateRequest destination, Set<TagDtos.TagCreateRequest> tags)implements Serializable {}
    public record FlightUpdateRequest(Long id, String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                      AirlineDtos.AirlineCreateRequest airline,
                                      AirportDtos.AirportCreateRequest origin, AirportDtos.AirportCreateRequest destination, Set<TagDtos.TagCreateRequest> tags )implements Serializable {}
    public record FlightResponse(Long id, String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                 AirlineDtos.AirlineResponse airline,
                                 AirportDtos.AirportResponse origin, AirportDtos.AirportResponse destination, Set<TagDtos.TagResponse> tags) implements Serializable {}
}
