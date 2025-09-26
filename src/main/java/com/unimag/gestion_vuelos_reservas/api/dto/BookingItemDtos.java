package com.unimag.gestion_vuelos_reservas.api.dto;

import com.unimag.gestion_vuelos_reservas.models.Cabin;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class BookingItemDtos {
    public record BookingItemCreateRequest(BigDecimal price, Integer segmentOrder, Cabin cabin,Long bookngId,Long flighId) implements Serializable {}
    public record BookingItemUpdateRequest(Long id, BigDecimal price, Integer segmentOrder, Cabin cabin,@Nullable Long flight_id ) implements Serializable {}
    public record BookingItemResponse(Long id, BigDecimal price, Integer segmentOrder, Cabin cabin,BookingDtos.BookingResponse bookingDto, FlightDtos.FlightResponse flightDto) implements Serializable {}
}
