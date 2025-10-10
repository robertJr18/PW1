package com.unimag.gestion_vuelos_reservas.api.dto;

import com.unimag.gestion_vuelos_reservas.models.Cabin;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


public class BookingItemDtos {
    public record BookingItemCreateRequest(@NotBlank BigDecimal price, @NotBlank Integer segmentOrder, @NotBlank Cabin cabin,@NotBlank Long flighId) implements Serializable {}
    public record BookingItemUpdateRequest(BigDecimal price, Integer segmentOrder, Cabin cabin,@Nullable Long flight_id ) implements Serializable {}
    public record BookingItemResponse(Long id, BigDecimal price, Integer segmentOrder, Cabin cabin, FlightDtos.FlightResponse flightDto) implements Serializable {}
}
