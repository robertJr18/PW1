package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.models.Booking;
import com.unimag.gestion_vuelos_reservas.models.BookingItem;
import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;
import com.unimag.gestion_vuelos_reservas.repositories.PassengerRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    private final PassengerMapper passengerMapper;
    private final BokingItemMapper bokingItemMapper;

    public BookingMapper(PassengerMapper passengerMapper, BokingItemMapper bokingItemMapper) {
        this.passengerMapper = passengerMapper;
        this.bokingItemMapper = bokingItemMapper;
    }

    public static Booking toEntity(BookingDtos.BookingCreateRequest request, Passenger passenger, List<BookingItem> items) {
        if (request == null) return null;
        if (passenger == null) throw  new IllegalArgumentException("Passenger is null");

        Booking booking = Booking.builder()
                .createdAt(request.createdAt())
                .passenger(passenger)
                .items(new ArrayList<>())
                .build();

        if (!CollectionUtils.isEmpty(items)) {
            items.forEach(booking::addItem);
        }
        return booking;
    }

    public static void updateEntity(Booking booking, Passenger passenger){
        if (booking == null) return;

        if (passenger != null) booking.setPassenger(passenger);
    }

    public BookingDtos.BookingResponse toResponse(Booking booking) {
        if (booking == null) return null;

        List<BookingItemDtos.BookingItemResponse> itemsResponse =
                CollectionUtils.isEmpty(booking.getItems())?
                new ArrayList<>() :
                booking.getItems().stream()
                        .map(bokingItemMapper:: toResponse)
                        .toList();
        return new BookingDtos.BookingResponse(
                booking.getId(),
                booking.getCreatedAt(),
                passengerMapper.toResponse(booking.getPassenger()),
                itemsResponse
        );

    }


}
