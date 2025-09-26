package com.unimag.gestion_vuelos_reservas.api.dto;

import java.io.Serializable;

public class PassengerDtos {
    public record PassengerCreateRequest(String firstName, String lastName, Long passenger_id) implements Serializable {}
    public record PassengerUpdateRequest(String firstName, String lastName) implements Serializable {}
    public record PassengerProfileDto(String phone, String countryCode) implements Serializable {}
    public record PassengerResponse(Long id, String fullname, String email, PassengerProfileDto profileDto) implements Serializable {}
}
