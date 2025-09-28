package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.api.dto.SeatInventoryDtos;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.SeatInventory;

public class SeatInventoryMapper {

    private final FlightMapper flightMapper;

    public SeatInventoryMapper(FlightMapper flightMapper) {
        this.flightMapper = flightMapper;
    }

    public static SeatInventory toEntity(SeatInventoryDtos.SeatInventoryCreateRequest request, Flight flight) {
        if (request == null) return null;

        if (flight == null) throw new NullPointerException("flight is null");

        return SeatInventory.builder()
                .cabin(request.cabin())
                .totalSeats(request.totalSeats())
                .availableSeats(request.availableSeats())
                .flight(flight)
                .build();
    }

    public static void UpdateEntity(SeatInventory seatInventory, SeatInventoryDtos.SeatInventoryUpdateRequest request, Flight flight){
        if (seatInventory == null || request == null) return;

        if (request.cabin() != null) {
            seatInventory.setCabin(request.cabin());
        }
        if (request.totalSeats() != null) {
            seatInventory.setTotalSeats(request.totalSeats());
        }
        if (request.availableSeats() != null) {
            seatInventory.setAvailableSeats(request.availableSeats());
        }
        if (flight != null) {
            seatInventory.setFlight(flight);
        }
    }

    public SeatInventoryDtos.SeatInventoryResponse toResponse(SeatInventory seatInventory) {
        if (seatInventory == null) return null;
        return new SeatInventoryDtos.SeatInventoryResponse(
                seatInventory.getId(),
                seatInventory.getCabin(),
                seatInventory.getTotalSeats(),
                seatInventory.getAvailableSeats(),
                flightMapper.toResponse(seatInventory.getFlight())
        );

    }
}
