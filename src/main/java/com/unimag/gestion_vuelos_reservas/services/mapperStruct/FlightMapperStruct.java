package com.unimag.gestion_vuelos_reservas.services.mapperStruct;

import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.Tag;
import org.mapstruct.*;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FlightMapperStruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "bookingItems", ignore = true)
    @Mapping(target = "seatInventories", ignore = true)
    Flight toEntity(FlightDtos.FlightCreateRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "bookingItems", ignore = true)
    @Mapping(target = "seatInventories", ignore = true)
    void updateEntity(@MappingTarget Flight entity, FlightDtos.FlightUpdateRequest dto);

    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "tags", ignore = true)
    FlightDtos.FlightResponse toResponse(Flight entity);


}

