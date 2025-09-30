package com.unimag.gestion_vuelos_reservas.services;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;

import java.util.List;

public interface BookingItemService {
    BookingItemDtos.BookingItemResponse createBookingItem(Long bookingId,BookingItemDtos.BookingItemCreateRequest request);
    BookingItemDtos.BookingItemResponse updateBookingItem(Long id,BookingItemDtos.BookingItemUpdateRequest request);
    BookingItemDtos.BookingItemResponse deleteBookingItem(long id);
    List<BookingItemDtos.BookingItemResponse> findItemsByBooking(Long bookingId);

}
