package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.SeatInventoryDtos;
import com.unimag.gestion_vuelos_reservas.models.Cabin;

import java.util.List;

public interface SeatInventoryService {
    SeatInventoryDtos.SeatInventoryResponse create(SeatInventoryDtos.SeatInventoryCreateRequest request);
    SeatInventoryDtos.SeatInventoryResponse getById(Long id);
    List<SeatInventoryDtos.SeatInventoryResponse> getAll();
    SeatInventoryDtos.SeatInventoryResponse update(Long id, SeatInventoryDtos.SeatInventoryUpdateRequest request);
    void delete(Long id);

    SeatInventoryDtos.SeatInventoryResponse findByFlightAndCabin(Long flightId, Cabin cabin);
    boolean hasAvailableSeats(Long flightId, Cabin cabin, Integer minSeats);
    boolean decrementAvailableSeats(Long flightId, Cabin cabin, int quantity);
    boolean incrementAvailableSeats(Long flightId, Cabin cabin, int quantity);
}
