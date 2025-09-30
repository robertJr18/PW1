package com.unimag.gestion_vuelos_reservas.services.impl;


import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.*;
import com.unimag.gestion_vuelos_reservas.services.FlightService;
import com.unimag.gestion_vuelos_reservas.services.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public FlightDtos.FlightResponse create(FlightDtos.FlightCreateRequest request) {
        Airline airline = airlineRepository.findById(request.airlineId())
                .orElseThrow(() -> new NotFoundException("Airline not found with id: " + request.airlineId()));

        Airport origin = airportRepository.findById(request.originId())
                .orElseThrow(() -> new NotFoundException("Origin airport not found with id: " + request.originId()));

        Airport destination = airportRepository.findById(request.destinationId())
                .orElseThrow(() -> new NotFoundException("Destination airport not found with id: " + request.destinationId()));

        Set<Tag> tags = new HashSet<>();
        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            tags = new HashSet<>(tagRepository.findAllById(request.tagIds()));
        }

        Flight flight = Flight.builder()
                .number(request.number())
                .departureTime(request.departureTime())
                .arrivalTime(request.arrivalTime())
                .airline(airline)
                .origin(origin)
                .destination(destination)
                .tags(tags)
                .build();

        Flight saved = flightRepository.save(flight);
        return FlightMapper.toResponse(saved);
    }

    @Override
    public FlightDtos.FlightResponse getById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + id));
        return FlightMapper.toResponse(flight);
    }

    @Override
    public List<FlightDtos.FlightResponse> getAll() {
        return flightRepository.findAll().stream()
                .map(FlightMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FlightDtos.FlightResponse update(Long id, FlightDtos.FlightUpdateRequest request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight not found with id: " + id));

        if (request.airlineId() != null && !request.airlineId().equals(flight.getAirline().getId())) {
            Airline airline = airlineRepository.findById(request.airlineId())
                    .orElseThrow(() -> new NotFoundException("Airline not found with id: " + request.airlineId()));
            flight.setAirline(airline);
        }

        if (request.originId() != null && !request.originId().equals(flight.getOrigin().getId())) {
            Airport origin = airportRepository.findById(request.originId())
                    .orElseThrow(() -> new NotFoundException("Origin airport not found with id: " + request.originId()));
            flight.setOrigin(origin);
        }

        if (request.destinationId() != null && !request.destinationId().equals(flight.getDestination().getId())) {
            Airport destination = airportRepository.findById(request.destinationId())
                    .orElseThrow(() -> new NotFoundException("Destination airport not found with id: " + request.destinationId()));
            flight.setDestination(destination);
        }

        if (request.number() != null) {
            flight.setNumber(request.number());
        }
        if (request.departureTime() != null) {
            flight.setDepartureTime(request.departureTime());
        }
        if (request.arrivalTime() != null) {
            flight.setArrivalTime(request.arrivalTime());
        }

        if (request.tagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.tagIds()));
            flight.setTags(tags);
        }

        Flight updated = flightRepository.save(flight);
        return FlightMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new NotFoundException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }

    @Override
    public Page<FlightDtos.FlightResponse> findByAirlineName(String airlineName, Pageable pageable) {
        return flightRepository.findByAirline_Name(airlineName, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public Page<FlightDtos.FlightResponse> searchFlights(
            String originCode,
            String destinationCode,
            OffsetDateTime from,
            OffsetDateTime to,
            Pageable pageable) {
        return flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
                        originCode, destinationCode, from, to, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public List<FlightDtos.FlightResponse> searchFlightsWithDetails(
            String originCode,
            String destinationCode,
            OffsetDateTime from,
            OffsetDateTime to) {
        return flightRepository.searchWithAssociations(originCode, destinationCode, from, to)
                .stream()
                .map(FlightMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightDtos.FlightResponse> findFlightsWithAllTags(Collection<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return List.of();
        }
        return flightRepository.findFlightsWithAllTags(tagNames, tagNames.size())
                .stream()
                .map(FlightMapper::toResponse)
                .collect(Collectors.toList());
    }
}
