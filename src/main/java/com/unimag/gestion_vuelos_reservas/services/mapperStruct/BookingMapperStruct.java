package com.unimag.gestion_vuelos_reservas.services.mapperStruct;


import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;
import com.unimag.gestion_vuelos_reservas.models.Booking;
import com.unimag.gestion_vuelos_reservas.models.BookingItem;
import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.services.mapper.BokingItemMapper;
import com.unimag.gestion_vuelos_reservas.services.mapper.PassengerMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring", // Genera un @Component de Spring
        uses = {PassengerMapper.class, BokingItemMapper.class}, // Usa otros mappers
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookingMapperStruct {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passenger", source = "passenger")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "createdAt", source = "request.createdAt")
    Booking toEntity(
            BookingDtos.BookingCreateRequest request,
            Passenger passenger,
            List<BookingItem> items
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "passenger", source = "passenger")
    void updateEntity(@MappingTarget Booking booking, Passenger passenger);

    @Mapping(target = "passengerId", source = "passenger.id")
    @Mapping(target = "passengerResponse", source = "passenger")
    @Mapping(target = "items", source = "items")
    BookingDtos.BookingResponse toResponse(Booking booking);

}
