package com.unimag.gestion_vuelos_reservas.api.controllers;

import com.unimag.gestion_vuelos_reservas.api.dto.PassengerDtos;
import com.unimag.gestion_vuelos_reservas.services.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
@Validated
public class PassengerController {
    private final PassengerService passengerService;
    @PostMapping
    public ResponseEntity<PassengerDtos.PassengerResponse> create(@Valid@RequestBody PassengerDtos.PassengerCreateRequest request,
                                                                  UriComponentsBuilder uriBuilder) {
        var body = passengerService.createPassenger(request);
        var location = uriBuilder.path("/api/passengers/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PassengerDtos.PassengerResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.get(id));
    }
    @GetMapping("/by-email")
    public ResponseEntity<PassengerDtos.PassengerResponse> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(passengerService.getByEmail(email));
    }
    @GetMapping("/by-email-with-profile")
    public ResponseEntity<PassengerDtos.PassengerResponse> getByEmailWithProfile(@RequestParam String email) {
        return ResponseEntity.ok(passengerService.getByEmailWithProfile(email));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<PassengerDtos.PassengerResponse> update(@PathVariable Long id,
                                                                  @Valid@RequestBody PassengerDtos.PassengerUpdateRequest request){
        return ResponseEntity.ok(passengerService.updatePassenger(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
