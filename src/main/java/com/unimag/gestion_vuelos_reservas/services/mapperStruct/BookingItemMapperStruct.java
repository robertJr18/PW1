package com.unimag.gestion_vuelos_reservas.services.mapperStruct;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.models.BookingItem;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {FlightMapperStruct.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookingItemMapperStruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "flight", source = "flight")
    BookingItem toEntity(BookingItemDtos.BookingItemCreateRequest request, Flight flight);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "flight", source = "flight")
    void updateEntity(
            @MappingTarget BookingItem item,
            BookingItemDtos.BookingItemUpdateRequest request,
            Flight flight
    );

    BookingItemDtos.BookingItemResponse toResponse(BookingItem item);
}
