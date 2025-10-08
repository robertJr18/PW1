package com.unimag.gestion_vuelos_reservas.services.mapperStruct;

import com.unimag.gestion_vuelos_reservas.api.dto.AirportDtos;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AirportMapperStruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originFlights", ignore = true)
    @Mapping(target = "destinationFlights", ignore = true)
    Airport toEntity(AirportDtos.AirportCreateRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originFlights", ignore = true)
    @Mapping(target = "destinationFlights", ignore = true)
    void updateEntity(@MappingTarget Airport entity, AirportDtos.AirportUpdateRequest dto);

    AirportDtos.AirportResponse toResponse(Airport entity);
}
