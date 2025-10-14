package com.unimag.gestion_vuelos_reservas.api.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public class BookingDtos {
    public  record BookingCreateRequest(@NotNull OffsetDateTime createdAt,@NotNull Long passengerId, List<BookingItemDtos.BookingItemCreateRequest> items) implements Serializable {}
    public  record BookingUpdateRequest(Long passenger_id) implements Serializable {}
    public  record BookingResponse(Long id, OffsetDateTime createdAt, PassengerDtos.PassengerResponse passengerDto,  List<BookingItemDtos.BookingItemResponse> items) implements Serializable {}
}
