package com.unimag.gestion_vuelos_reservas.services.mapperStruct;

import com.unimag.gestion_vuelos_reservas.api.dto.PassengerDtos;
import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PassengerMapperStruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "fullName", source = "fullname")
    @Mapping(target = "profile", source = "profileDto")
    Passenger toEntity(PassengerDtos.PassengerCreateRequest request);

    PassengerProfile toProfile(PassengerDtos.PassengerProfileDto profileDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "fullName", source = "dto.fullname")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "profile", source = "profile")
    void updateEntity(
            @MappingTarget Passenger passenger,
            PassengerDtos.PassengerUpdateRequest dto,
            PassengerProfile profile
    );

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullname", source = "fullName")
    @Mapping(target = "profileDto", source = "profile")
    PassengerDtos.PassengerResponse toResponse(Passenger passenger);

    PassengerDtos.PassengerProfileDto toProfileDto(PassengerProfile profile);
}
