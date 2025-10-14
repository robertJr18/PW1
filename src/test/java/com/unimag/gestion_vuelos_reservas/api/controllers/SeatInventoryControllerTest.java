package com.unimag.gestion_vuelos_reservas.api.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.unimag.gestion_vuelos_reservas.api.dto.SeatInventoryDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Cabin;
import com.unimag.gestion_vuelos_reservas.services.SeatInventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SeatInventoryController.class)
public class SeatInventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SeatInventoryService seatInventoryService;

    @Test
    void create_shouldReturn201() throws Exception {
        var request = new SeatInventoryDtos.SeatInventoryCreateRequest(Cabin.ECONOMY, 150, 150, 1L);
        var flightResponse = new FlightDtos.FlightResponse(1L, "AV123", null, null, null, null, null, null);
        var response = new SeatInventoryDtos.SeatInventoryResponse(1L, Cabin.ECONOMY, 150, 150, flightResponse);

        when(seatInventoryService.create(any(SeatInventoryDtos.SeatInventoryCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/seat-inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/seat-inventories/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.totalSeats").value(150))
                .andExpect(jsonPath("$.availableSeats").value(150));

        verify(seatInventoryService).create(any(SeatInventoryDtos.SeatInventoryCreateRequest.class));
    }

    @Test
    void getById_shouldReturn200() throws Exception {
        var flightResponse = new FlightDtos.FlightResponse(1L, "AV123", null, null, null, null, null, null);
        var response = new SeatInventoryDtos.SeatInventoryResponse(1L, Cabin.ECONOMY, 150, 150, flightResponse);

        when(seatInventoryService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/seat-inventories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.totalSeats").value(150))
                .andExpect(jsonPath("$.availableSeats").value(150));

        verify(seatInventoryService).getById(1L);
    }

    @Test
    void getById_shouldReturn404() throws Exception {
        when(seatInventoryService.getById(999L)).thenThrow(new NotFoundException("SeatInventory not found"));

        mockMvc.perform(get("/api/seat-inventories/999"))
                .andExpect(status().isNotFound());

        verify(seatInventoryService).getById(999L);
    }

    @Test
    void getAll_shouldReturn200() throws Exception {
        var flightResponse = new FlightDtos.FlightResponse(1L, "AV123", null, null, null, null, null, null);
        var inventory1 = new SeatInventoryDtos.SeatInventoryResponse(1L, Cabin.ECONOMY, 150, 150, flightResponse);
        var inventory2 = new SeatInventoryDtos.SeatInventoryResponse(2L, Cabin.BUSINESS, 30, 30, flightResponse);
        var inventories = List.of(inventory1, inventory2);

        when(seatInventoryService.getAll()).thenReturn(inventories);

        mockMvc.perform(get("/api/seat-inventories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cabin").value("ECONOMY"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].cabin").value("BUSINESS"));

        verify(seatInventoryService).getAll();
    }

    @Test
    void findByFlightAndCabin_shouldReturn200() throws Exception {
        var flightResponse = new FlightDtos.FlightResponse(1L, "AV123", null, null, null, null, null, null);
        var response = new SeatInventoryDtos.SeatInventoryResponse(1L, Cabin.ECONOMY, 150, 150, flightResponse);

        when(seatInventoryService.findByFlightAndCabin(1L, Cabin.ECONOMY)).thenReturn(response);

        mockMvc.perform(get("/api/seat-inventories/by-flight-cabin")
                        .param("flightId", "1")
                        .param("cabin", "ECONOMY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.totalSeats").value(150))
                .andExpect(jsonPath("$.availableSeats").value(150));

        verify(seatInventoryService).findByFlightAndCabin(1L, Cabin.ECONOMY);
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var request = new SeatInventoryDtos.SeatInventoryUpdateRequest(Cabin.ECONOMY, 150, 140, 1L);
        var flightResponse = new FlightDtos.FlightResponse(1L, "AV123", null, null, null, null, null, null);
        var response = new SeatInventoryDtos.SeatInventoryResponse(1L, Cabin.ECONOMY, 150, 140, flightResponse);

        when(seatInventoryService.update(eq(1L), any(SeatInventoryDtos.SeatInventoryUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/seat-inventories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.totalSeats").value(150))
                .andExpect(jsonPath("$.availableSeats").value(140));

        verify(seatInventoryService).update(eq(1L), any(SeatInventoryDtos.SeatInventoryUpdateRequest.class));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/seat-inventories/1"))
                .andExpect(status().isNoContent());

        verify(seatInventoryService).delete(1L);
    }
}
