package com.unimag.gestion_vuelos_reservas.api.dto;

import java.io.Serializable;

public class PassengerDtos {
    public record PassengerCreateRequest(String fullname, String email, PassengerProfileDto profileDto) implements Serializable {}
    public record PassengerUpdateRequest(Long id , String fullname, String email, PassengerProfileDto profileDto) implements Serializable {}
    public record PassengerProfileDto(String phone, String countryCode) implements Serializable {}
    public record PassengerResponse(Long id, String fullname, String email, PassengerProfileDto profileDto) implements Serializable {}
}
