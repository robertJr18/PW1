package com.unimag.gestion_vuelos_reservas.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public class BookingDtos {
    public  record BookingCreateRequest(OffsetDateTime createdAt, PassengerDtos.PassengerCreateRequest passengerDto ) implements Serializable {}
    public  record BookingUpdateRequest(Long id, Long passenger_id) implements Serializable {}
    public  record BookingResponse(Long id, OffsetDateTime createdAt, PassengerDtos.PassengerResponse passengerDto) implements Serializable {}
}
