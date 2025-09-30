package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.AirlineDtos;

public interface AirlineService {
    AirlineDtos.AirlineResponse create(AirlineDtos.AirlineCreateRequest request);
    AirlineDtos.AirlineResponse get(Long id);

    AirlineDtos.AirlineResponse getByCode(String code);
    AirlineDtos.AirlineResponse update(Long id, AirlineDtos.AirlineUpdateRequest request);
    void delete(Long id);
}
