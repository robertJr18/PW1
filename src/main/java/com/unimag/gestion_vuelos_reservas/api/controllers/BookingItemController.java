package com.unimag.gestion_vuelos_reservas.api.controllers;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.services.BookingItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/bookings/{bookingId}/items")
@RequiredArgsConstructor
@Validated
public class BookingItemController {
    private final BookingItemService service;
    @PostMapping
    public ResponseEntity<BookingItemDtos.BookingItemResponse> create(@PathVariable Long bookingId,
                                                                      @Valid@RequestBody BookingItemDtos.BookingItemCreateRequest request,
                                                                      UriComponentsBuilder uriBuilder){
        var body = service.createBookingItem(bookingId, request);
        var location = uriBuilder.path("/api/bookings/{bookingId}/items/{itemId}").
                buildAndExpand(bookingId,body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping
    public ResponseEntity<List<BookingItemDtos.BookingItemResponse>> list(@PathVariable Long bookingId){
        return ResponseEntity.ok(service.findItemsByBooking(bookingId));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<BookingItemDtos.BookingItemResponse> update(@PathVariable Long id,
                                                                      @Valid@RequestBody BookingItemDtos.BookingItemUpdateRequest request){
        return ResponseEntity.ok(service.updateBookingItem(id,request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteBookingItem(id);
        return ResponseEntity.noContent().build();
    }

}
