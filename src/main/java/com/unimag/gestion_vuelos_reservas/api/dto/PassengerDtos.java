package com.unimag.gestion_vuelos_reservas.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.Name;

import java.io.Serializable;

public class PassengerDtos {
    public record PassengerCreateRequest(@NotBlank String fullname, @NotBlank@Email String email, PassengerProfileDto profileDto) implements Serializable {}
    public record PassengerUpdateRequest(String fullname, String email, PassengerProfileDto profileDto) implements Serializable {}
    public record PassengerProfileDto(@Size(max = 25) String phone,@Size(max=10) String countryCode) implements Serializable {}
    public record PassengerResponse(Long id, String fullname, String email, PassengerProfileDto profileDto) implements Serializable {}
}
