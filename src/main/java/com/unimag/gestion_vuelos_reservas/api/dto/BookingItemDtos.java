package com.unimag.gestion_vuelos_reservas.api.dto;

import com.unimag.gestion_vuelos_reservas.models.Cabin;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


public class BookingItemDtos {
    public record BookingItemCreateRequest(@Min(1) BigDecimal price,@Min(1) Integer segmentOrder,Cabin cabin,@NotNull Long flighId) implements Serializable {}
    public record BookingItemUpdateRequest(BigDecimal price, Integer segmentOrder, Cabin cabin,@Nullable Long flight_id ) implements Serializable {}
    public record BookingItemResponse(Long id, BigDecimal price, Integer segmentOrder, Cabin cabin, FlightDtos.FlightResponse flightDto) implements Serializable {}
}
