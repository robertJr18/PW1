package com.unimag.gestion_vuelos_reservas.services.mapperStruct;

import com.unimag.gestion_vuelos_reservas.api.dto.AirlineDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AirlineMapperStruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    Airline toEntity(AirlineDtos.AirlineCreateRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    void updateEntity(@MappingTarget Airline entity, AirlineDtos.AirlineUpdateRequest dto);

    AirlineDtos.AirlineResponse toResponse(Airline entity);

    @Mapping(target = "flights", ignore = true)
    AirlineDtos.AirlineDetailResponse toDetailResponse(Airline entity);

}
