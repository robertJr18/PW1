package com.unimag.gestion_vuelos_reservas.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class BookingDtos {
    public  record BookingCreateRequest(OffsetDateTime createdAt, Long passenger_id, List<BookingItemDtos.BookingItemCreateRequest> itemsDto ) implements Serializable {}
    public  record BookingUpdateRequest(Long id, Long passenger_id,List<BookingItemDtos.BookingItemCreateRequest> itemsDto) implements Serializable {}
    public  record BookingResponse(Long id, OffsetDateTime createdAt, PassengerDtos.PassengerResponse passengerDto, List<BookingItemDtos.BookingItemResponse> itemsDto) implements Serializable {}
}
