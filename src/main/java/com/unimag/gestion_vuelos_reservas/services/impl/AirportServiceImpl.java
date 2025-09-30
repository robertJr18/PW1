package com.unimag.gestion_vuelos_reservas.services.impl;

import com.unimag.gestion_vuelos_reservas.api.dto.AirportDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.repositories.AirportRepository;
import com.unimag.gestion_vuelos_reservas.services.AirportService;
import com.unimag.gestion_vuelos_reservas.services.mapper.AirportMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    @Override
    public AirportDtos.AirportResponse create(AirportDtos.AirportCreateRequest request) {
        String code = request.code().trim().toUpperCase();

        airportRepository.findByCode(code).ifPresent(a -> {
            throw new IllegalStateException("Airport code already exists: " + code);
        });

        Airport toSave = AirportMapper.toEntity(request);
        toSave.setCode(code);

        Airport saved = airportRepository.save(toSave);
        return AirportMapper.toResponse(saved);
    }

    @Override
    public AirportDtos.AirportResponse get(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Airport not found: id=" + id));
        return AirportMapper.toResponse(airport);
    }

    @Override
    public AirportDtos.AirportResponse getByCode(String rawCode) {
        String code = rawCode.trim().toUpperCase();
        Airport airport = airportRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Airport not found: code=" + code));
        return AirportMapper.toResponse(airport);
    }

    @Override
    public AirportDtos.AirportResponse update(Long id, AirportDtos.AirportUpdateRequest request) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Airport not found: id=" + id));

        String newCode = request.code() == null ? null : request.code().trim().toUpperCase();
        if (newCode == null || newCode.isBlank()) {
            throw new IllegalArgumentException("Airport code cannot be blank");
        }

        if (!airport.getCode().equalsIgnoreCase(newCode)) {
            airportRepository.findByCode(newCode).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalStateException("Airport code already exists: " + newCode);
                }
            });
        }

        airport.setCode(newCode);
        airport.setName(request.name());
        airport.setCity(request.city());

        Airport updated = airportRepository.save(airport);

        return AirportMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!airportRepository.existsById(id)) {
            throw new NotFoundException("Airport not found: id=" + id);
        }
        airportRepository.deleteById(id);
    }
}
