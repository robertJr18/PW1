package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.AirportDtos;

public interface AirportService {
    AirportDtos.AirportResponse create(AirportDtos.AirportCreateRequest request);
    AirportDtos.AirportResponse  get(Long id);

    AirportDtos.AirportResponse getByCode(String code);
    AirportDtos.AirportResponse  update(Long id, AirportDtos.AirportUpdateRequest request);
    void delete(Long id);
}
