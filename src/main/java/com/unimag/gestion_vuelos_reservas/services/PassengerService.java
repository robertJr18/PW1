package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.PassengerDtos;

public interface PassengerService {
    PassengerDtos.PassengerCreateRequest createPassenger(PassengerDtos.PassengerCreateRequest passengerCreateRequest);
    PassengerDtos.PassengerUpdateRequest updatePassenger(PassengerDtos.PassengerUpdateRequest passengerUpdateRequest);
    PassengerDtos.PassengerResponse get(Long id);
    PassengerDtos.PassengerResponse getByEmail(String email);
    void delete(Long id);
}
