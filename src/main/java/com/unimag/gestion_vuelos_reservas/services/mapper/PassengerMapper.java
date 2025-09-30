package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.api.dto.PassengerDtos;
import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;
import org.springframework.stereotype.Component;


public class PassengerMapper {
    public static Passenger toEntity(PassengerDtos.PassengerCreateRequest passengerCreateRequest) {
        var profile = (passengerCreateRequest.profileDto() == null ) ? null :
                PassengerProfile.builder().phone(passengerCreateRequest.profileDto().phone())
                        .countryCode(passengerCreateRequest.profileDto().countryCode()).build();
        return Passenger.builder().fullName(passengerCreateRequest.fullname())
                .email(passengerCreateRequest.email()).profile(profile).build();


    }


    public static void UpdateEntity(PassengerDtos.PassengerUpdateRequest dto, Passenger passenger, PassengerProfile profile) {
        if (passenger == null || dto == null) return;
        if (dto.fullname() != null) {
            passenger.setFullName(dto.fullname());
        }
        if (dto.email() != null) {
            passenger.setEmail(dto.email());
        }
        if (profile != null) {
            passenger.setProfile(profile);

        }
    }

    public static PassengerDtos.PassengerResponse toResponse(Passenger passenger) {
        var profileDto= passenger.getProfile() == null ? null :
                new PassengerDtos.PassengerProfileDto(passenger.getProfile().getPhone(), passenger.getProfile().getCountryCode());
        return new PassengerDtos.PassengerResponse((long) passenger.getId(),passenger.getFullName(),passenger.getEmail(),profileDto);

    }
}
