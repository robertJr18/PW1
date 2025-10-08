package com.unimag.gestion_vuelos_reservas.services.mapperStruct;

import com.unimag.gestion_vuelos_reservas.api.dto.SeatInventoryDtos;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.SeatInventory;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {FlightMapperStruct.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SeatInventoryMapperStruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", source = "flight")
    SeatInventory toEntity(SeatInventoryDtos.SeatInventoryCreateRequest request, Flight flight);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", source = "flight")
    void updateEntity(
            @MappingTarget SeatInventory seatInventory,
            SeatInventoryDtos.SeatInventoryUpdateRequest request,
            Flight flight
    );

    SeatInventoryDtos.SeatInventoryResponse toResponse(SeatInventory seatInventory);
}