package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;

import java.util.List;

public interface BookingService {
    BookingDtos.BookingResponse createBooking(BookingDtos.BookingCreateRequest request);
    BookingDtos.BookingResponse updateBooking(Long id,BookingDtos.BookingUpdateRequest request);
    BookingDtos.BookingResponse getBookingId(long id);
    void deleteById(long id);
    List<BookingDtos.BookingResponse> finBookingByPassengerEmail(String email);
    BookingDtos.BookingResponse getBookingWithDetails(long id);
}
