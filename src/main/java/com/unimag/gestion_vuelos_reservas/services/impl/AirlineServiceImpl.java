package com.unimag.gestion_vuelos_reservas.services.impl;

import com.unimag.gestion_vuelos_reservas.api.dto.AirlineDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.repositories.AirlineRepository;
import com.unimag.gestion_vuelos_reservas.services.AirlineService;
import com.unimag.gestion_vuelos_reservas.services.mapper.AirlineMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    private final AirlineMapper airlineMapper;

    @Override
    public AirlineDtos.AirlineResponse create(AirlineDtos.AirlineCreateRequest request) {
        String code = request.code().trim().toUpperCase();

        airlineRepository.findByCode(code).ifPresent(a -> {
            throw new IllegalStateException("Airline code already exists: " + code);
        });

        Airline toSave = airlineMapper.toEntity(request);
        toSave.setCode(code);

        Airline saved = airlineRepository.save(toSave);
        return airlineMapper.toResponse(saved);
    }

    @Override
    public AirlineDtos.AirlineResponse get(Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Airline not found: id=" + id));
        return airlineMapper.toResponse(airline);
    }

    @Override
    public AirlineDtos.AirlineResponse getByCode(String rawCode) {
        String code = rawCode.trim().toUpperCase();
        Airline airline = airlineRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Airline not found: code=" + code));
        return airlineMapper.toResponse(airline);
    }

    @Override
    public AirlineDtos.AirlineResponse update(Long id, AirlineDtos.AirlineUpdateRequest request) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Airline not found: id=" + id));

        String newCode = request.code() == null ? null : request.code().trim().toUpperCase();
        if (newCode == null || newCode.isBlank()) {
            throw new IllegalArgumentException("Airline code cannot be blank");
        }

        if (!airline.getCode().equalsIgnoreCase(newCode)) {
            airlineRepository.findByCode(newCode).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalStateException("Airline code already exists: " + newCode);
                }
            });
        }

        airline.setCode(newCode);
        airline.setName(request.name());

        Airline updated = airlineRepository.save(airline);

        return airlineMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!airlineRepository.existsById(id)) {
            throw new NotFoundException("Airline not found: id=" + id);
        }
        airlineRepository.deleteById(id);
    }

    // ==== Helpers ====
    private boolean equalsIgnoreCaseSafe(String a, String b) {
        if (a == null) return b == null;
        return a.equalsIgnoreCase(b == null ? null : b);
    }
}
