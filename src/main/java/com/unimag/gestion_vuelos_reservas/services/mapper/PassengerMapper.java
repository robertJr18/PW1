package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.api.dto.PassengerDtos;
import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;

public class PassengerMapper {
    public static Passenger toEntity(PassengerDtos.PassengerCreateRequest passengerCreateRequest) {
        var profile = (passengerCreateRequest.profileDto() == null ) ? null :
                PassengerProfile.builder().phone(passengerCreateRequest.profileDto().phone())
                        .CountryCode(passengerCreateRequest.profileDto().countryCode()).build();
        return Passenger.builder().fullName(passengerCreateRequest.fullname())
                .email(passengerCreateRequest.email()).profile(profile).build();


    }


    public static Passenger toEntity(PassengerDtos.PassengerUpdateRequest dto){
        var profile = (dto.profileDto() == null ) ? null :
                PassengerProfile.builder().phone(dto.profileDto().phone())
                        .CountryCode(dto.profileDto().countryCode()).build();
        return Passenger.builder().id(Math.toIntExact(dto.id())).fullName(dto.fullname())
                .email(dto.email()).profile(profile).build();

    }

    public static PassengerDtos.PassengerResponse toDto(Passenger passenger) {
        var profileDto= passenger.getProfile() == null ? null :
                new PassengerDtos.PassengerProfileDto(passenger.getProfile().getPhone(), passenger.getProfile().getCountryCode());
        return new PassengerDtos.PassengerResponse((long) passenger.getId(),passenger.getFullName(),passenger.getEmail(),profileDto);

    }
}
