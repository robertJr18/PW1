package com.unimag.gestion_vuelos_reservas.services.mapperStruct;


import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;
import com.unimag.gestion_vuelos_reservas.models.Booking;
import com.unimag.gestion_vuelos_reservas.models.BookingItem;
import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.services.mapper.BokingItemMapper;
import com.unimag.gestion_vuelos_reservas.services.mapper.PassengerMapper;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring", // Genera un @Component de Spring
        uses = {PassengerMapper.class, BokingItemMapper.class}, // Usa otros mappers
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookingMapper {

    /**
     * Convierte BookingCreateRequest a Booking
     * Mapea passenger y items manualmente
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passenger", source = "passenger")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "createdAt", source = "request.createdAt")
    Booking toEntity(
            BookingDtos.BookingCreateRequest request,
            Passenger passenger,
            List<BookingItem> items
    );

    /**
     * Lógica personalizada después del mapeo
     * Valida que passenger no sea null y establece relaciones bidireccionales
     */
    @AfterMapping
    default void afterToEntity(
            @MappingTarget Booking booking,
            BookingDtos.BookingCreateRequest request,
            Passenger passenger,
            List<BookingItem> items
    ) {
        if (passenger == null) {
            throw new IllegalArgumentException("Passenger is null");
        }

        // Inicializar lista si es null
        if (booking.getItems() == null) {
            booking.setItems(new ArrayList<>());
        }

        // Establecer relación bidireccional (si tu método addItem lo hace)
        if (!CollectionUtils.isEmpty(items)) {
            booking.getItems().clear();
            items.forEach(booking::addItem);
        }
    }

    /**
     * Actualiza un Booking existente con un nuevo Passenger
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "passenger", source = "passenger")
    void updateEntity(@MappingTarget Booking booking, Passenger passenger);

    /**
     * Convierte Booking a BookingResponse
     * MapStruct automáticamente usa PassengerMapper y BookingItemMapper
     */
    @Mapping(target = "passengerId", source = "passenger.id")
    @Mapping(target = "passengerResponse", source = "passenger")
    @Mapping(target = "items", source = "items")
    BookingDtos.BookingResponse toResponse(Booking booking);

    /**
     * Manejo de listas vacías en la respuesta
     */
    @AfterMapping
    default void afterToResponse(
            @MappingTarget BookingDtos.BookingResponse response,
            Booking booking
    ) {
        if (CollectionUtils.isEmpty(booking.getItems())) {
            response.items();
        }
    }
}
