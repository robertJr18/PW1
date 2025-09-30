package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.PassengerDtos;

public interface PassengerService {
    PassengerDtos.PassengerResponse createPassenger(PassengerDtos.PassengerCreateRequest passengerCreateRequest);
    PassengerDtos.PassengerResponse updatePassenger(Long id,PassengerDtos.PassengerUpdateRequest passengerUpdateRequest);
    PassengerDtos.PassengerResponse get(Long id);
    PassengerDtos.PassengerResponse getByEmail(String email);
    PassengerDtos.PassengerResponse getByEmailWithProfile(String email);
    void delete(Long id);
}