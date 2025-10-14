package com.unimag.gestion_vuelos_reservas.api.controllers;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;
import com.unimag.gestion_vuelos_reservas.services.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService service;

    @PostMapping
    public ResponseEntity<BookingDtos.BookingResponse> create(@Valid@RequestBody BookingDtos.BookingCreateRequest request,
                                                              UriComponentsBuilder uriBuilder){
        var body = service.createBooking(request);
        var location = uriBuilder.path("/api/bookings/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingDtos.BookingResponse> get(@PathVariable long id){
        return ResponseEntity.ok(service.getBookingId(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<BookingDtos.BookingResponse> update(@PathVariable long id,
                                                              @Valid  @RequestBody BookingDtos.BookingUpdateRequest request){
        return ResponseEntity.ok(service.updateBooking(id, request));
    }
    @GetMapping("/by-passenger")
    public ResponseEntity<List<BookingDtos.BookingResponse>> findByPassengerEmail(@RequestParam String email){
        return  ResponseEntity.ok(service.finBookingByPassengerEmail(email));
    }
    @GetMapping("/booking-with-details/{id}")
    public ResponseEntity<BookingDtos.BookingResponse> getBookingWithDetails(@PathVariable long id){
        return ResponseEntity.ok(service.getBookingWithDetails(id));
    }
}
