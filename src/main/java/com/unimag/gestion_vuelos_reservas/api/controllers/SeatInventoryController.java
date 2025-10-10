package com.unimag.gestion_vuelos_reservas.api.controllers;

import com.unimag.gestion_vuelos_reservas.api.dto.SeatInventoryDtos;
import com.unimag.gestion_vuelos_reservas.models.Cabin;
import com.unimag.gestion_vuelos_reservas.services.SeatInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/seat-inventories")
@RequiredArgsConstructor
@Validated
public class SeatInventoryController {
    private final SeatInventoryService service;

    @PostMapping
    public ResponseEntity<SeatInventoryDtos.SeatInventoryResponse> create(@Valid @RequestBody SeatInventoryDtos.SeatInventoryCreateRequest request,
                                                                          UriComponentsBuilder uriBuilder) {

        var body = service.create(request);
        var location = uriBuilder.path("/api/seat-inventories/{id}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatInventoryDtos.SeatInventoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryDtos.SeatInventoryResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/by-flight-cabin")
    public ResponseEntity<SeatInventoryDtos.SeatInventoryResponse> findByFlightAndCabin(
            @RequestParam Long flightId,
            @RequestParam Cabin cabin) {
        return ResponseEntity.ok(service.findByFlightAndCabin(flightId, cabin));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SeatInventoryDtos.SeatInventoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SeatInventoryDtos.SeatInventoryUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
