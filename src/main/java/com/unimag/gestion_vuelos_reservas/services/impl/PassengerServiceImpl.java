package com.unimag.gestion_vuelos_reservas.services.impl;

import com.unimag.gestion_vuelos_reservas.api.dto.PassengerDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;
import com.unimag.gestion_vuelos_reservas.repositories.PassengerProfileRepository;
import com.unimag.gestion_vuelos_reservas.repositories.PassengerRepository;
import com.unimag.gestion_vuelos_reservas.services.PassengerService;
import com.unimag.gestion_vuelos_reservas.services.mapper.PassengerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service@Transactional@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerProfileRepository passengerProfileRepository;

    @Override
    public PassengerDtos.PassengerResponse createPassenger(PassengerDtos.PassengerCreateRequest request) {
        if (request == null)throw new NotFoundException("Passenger not found");
        if (request.email() == null)throw new NotFoundException("Passenger email not found");
        passengerRepository.findByEmailIgnoreCase(request.email()).ifPresent(passenger -> {throw new IllegalArgumentException("Passenger email already exists");});
        PassengerProfile passengerProfile = null;
        if (request.profileDto() != null){
            passengerProfile = PassengerProfile.builder().phone(request.profileDto().phone())
                    .countryCode(request.profileDto().countryCode()).build();
        }
        Passenger passenger = PassengerMapper.toEntity(request);
        passengerRepository.save(passenger);
        return PassengerMapper.toResponse( passengerRepository.save(passenger));

    }

    @Override
    public PassengerDtos.PassengerResponse updatePassenger(PassengerDtos.PassengerUpdateRequest request) {
        if (request == null) throw new IllegalArgumentException("PassengerUpdateRequest cannot be null");

        Passenger passenger = passengerRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException("Passenger not found with id: " + request.id()));

        // Validar email si se cambia
        if (request.email() != null && !request.email().equalsIgnoreCase(passenger.getEmail())) {
            passengerRepository.findByEmailIgnoreCase(request.email())
                    .ifPresent(p -> { throw new IllegalArgumentException("Email already in use: " + request.email()); });
            passenger.setEmail(request.email());
        }

        if (request.fullname() != null) {
            passenger.setFullName(request.fullname());
        }

        if (request.profileDto() != null) {
            PassengerProfile profile = passenger.getProfile();
            if (profile == null) {
                profile = new PassengerProfile();
                passenger.setProfile(profile);
            }
            profile.setPhone(request.profileDto().phone());
            profile.setCountryCode(request.profileDto().countryCode());
        }

        Passenger updated = passengerRepository.save(passenger);
        return PassengerMapper.toResponse(updated);
    }

    @Override
    public PassengerDtos.PassengerResponse get(Long id) {
        Passenger passenger = passengerRepository.findById(id).orElseThrow(() -> new NotFoundException("Passenger not found with id: " + id));
        return PassengerMapper.toResponse(passenger);
    }

    @Override
    public PassengerDtos.PassengerResponse getByEmail(String email) {
        Passenger passenger = passengerRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("Passenger not found with email: " + email));
        return PassengerMapper.toResponse(passenger);
    }

    @Override
    public PassengerDtos.PassengerResponse getByEmailWithProfile(String email) {
        Passenger passenger = passengerRepository.findByEmailWithProfileIgnoreCase(email).orElseThrow(() -> new NotFoundException("Passenger not found with email: " + email));
        return PassengerMapper.toResponse(passenger);
    }

    @Override
    public void delete(Long id) {
        Passenger passenger = passengerRepository.findById(id).orElseThrow(() -> new NotFoundException("Passenger not found with id: " + id));
        passengerRepository.delete(passenger);
    }
}
