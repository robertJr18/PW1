package com.unimag.gestion_vuelos_reservas.services.impl;

import com.unimag.gestion_vuelos_reservas.api.dto.SeatInventoryDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Cabin;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.SeatInventory;
import com.unimag.gestion_vuelos_reservas.repositories.FlightRepository;
import com.unimag.gestion_vuelos_reservas.repositories.SeatInventoryRepository;
import com.unimag.gestion_vuelos_reservas.services.SeatInventoryService;
import com.unimag.gestion_vuelos_reservas.services.mapper.SeatInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRepository seatInventoryRepository;
    private final FlightRepository flightRepository;

    @Override
    @Transactional
    public SeatInventoryDtos.SeatInventoryResponse create(SeatInventoryDtos.SeatInventoryCreateRequest request) {
        Flight flight = flightRepository.findById(request.flightId())
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + request.flightId()));

        // Validar que availableSeats no sea mayor que totalSeats
        if (request.availableSeats() > request.totalSeats()) {
            throw new IllegalArgumentException("Available seats cannot exceed total seats");
        }

        // Validar que no sea negativo
        if (request.availableSeats() < 0 || request.totalSeats() < 0) {
            throw new IllegalArgumentException("Seats cannot be negative");
        }

        SeatInventory seatInventory = SeatInventoryMapper.toEntity(request, flight);

        SeatInventory saved = seatInventoryRepository.save(seatInventory);
        return SeatInventoryMapper.toResponse(saved);
    }

    @Override
    public SeatInventoryDtos.SeatInventoryResponse getById(Long id) {
        SeatInventory seatInventory = seatInventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SeatInventory not found with id: " + id));
        return SeatInventoryMapper.toResponse(seatInventory);
    }

    @Override
    public List<SeatInventoryDtos.SeatInventoryResponse> getAll() {
        return seatInventoryRepository.findAll().stream()
                .map(SeatInventoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SeatInventoryDtos.SeatInventoryResponse update(Long id, SeatInventoryDtos.SeatInventoryUpdateRequest request) {
        SeatInventory seatInventory = seatInventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SeatInventory not found with id: " + id));

        // Validar y actualizar vuelo si cambió
        Flight flight = null;
        if (request.flightId() != null && !request.flightId().equals(seatInventory.getFlight().getId())) {
            flight = flightRepository.findById(request.flightId())
                    .orElseThrow(() -> new NotFoundException("Flight not found with id: " + request.flightId()));
        }

        // Validar campos antes de actualizar
        if (request.totalSeats() != null && request.totalSeats() < 0) {
            throw new IllegalArgumentException("Total seats cannot be negative");
        }
        if (request.availableSeats() != null) {
            if (request.availableSeats() < 0) {
                throw new IllegalArgumentException("Available seats cannot be negative");
            }
            Integer totalSeats = request.totalSeats() != null ? request.totalSeats() : seatInventory.getTotalSeats();
            if (request.availableSeats() > totalSeats) {
                throw new IllegalArgumentException("Available seats cannot exceed total seats");
            }
        }

        SeatInventoryMapper.UpdateEntity(seatInventory, request, flight);

        SeatInventory updated = seatInventoryRepository.save(seatInventory);
        return SeatInventoryMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!seatInventoryRepository.existsById(id)) {
            throw new NotFoundException("SeatInventory not found with id: " + id);
        }
        seatInventoryRepository.deleteById(id);
    }

    @Override
    public SeatInventoryDtos.SeatInventoryResponse findByFlightAndCabin(Long flightId, Cabin cabin) {
        SeatInventory seatInventory = seatInventoryRepository.findByFlight_IdAndCabin(flightId, cabin)
                .orElseThrow(() -> new NotFoundException(
                        "SeatInventory not found for flight " + flightId + " and cabin " + cabin));
        return SeatInventoryMapper.toResponse(seatInventory);
    }

    @Override
    public boolean hasAvailableSeats(Long flightId, Cabin cabin, Integer minSeats) {
        return seatInventoryRepository.hasAvailableSeats(flightId, cabin, minSeats);
    }

    @Override
    @Transactional
    public boolean decrementAvailableSeats(Long flightId, Cabin cabin, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        int updated = seatInventoryRepository.decrementAvailableSeats(flightId, cabin, quantity);
        return updated > 0;
    }

    @Override
    @Transactional
    public boolean incrementAvailableSeats(Long flightId, Cabin cabin, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        int updated = seatInventoryRepository.incrementAvailableSeats(flightId, cabin, quantity);
        return updated > 0;
    }
}
