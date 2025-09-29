package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.models.BookingItem;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BokingItemMapper {

    public static BookingItem toEntity(BookingItemDtos.BookingItemCreateRequest request, Flight flight) {
        if (request == null) return null;
        if (flight == null) throw new IllegalArgumentException("flight is null");

        return BookingItem.builder()
                .price(request.price())
                .segmentOrder(request.segmentOrder())
                .cabin(request.cabin())
                .flight(flight)
                .build();
    }

    public static void updateEntity(BookingItem item, BookingItemDtos.BookingItemUpdateRequest request, Flight flight) {
        if (item == null || request == null ) return;
        if (request.price() != null) {
            item.setPrice(request.price());
        }
        if (request.segmentOrder() != null) {
            item.setSegmentOrder(request.segmentOrder());
        }
        if (request.cabin() != null) {
            item.setCabin(request.cabin());
        }
        if (flight != null) {
            item.setFlight(flight);
        }

    }

    public static  BookingItemDtos.BookingItemResponse toResponse(BookingItem item) {
        if (item == null) return null;
        return new BookingItemDtos.BookingItemResponse(
                item.getId(), item.getPrice(), item.getSegmentOrder(),
                item.getCabin(), FlightMapper.toResponse(item.getFlight())
        );

    }

}
