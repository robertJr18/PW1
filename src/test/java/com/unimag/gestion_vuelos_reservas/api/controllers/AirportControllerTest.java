package com.unimag.gestion_vuelos_reservas.api.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.unimag.gestion_vuelos_reservas.api.dto.AirportDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.services.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AirportController.class)
public class AirportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AirportService airportService;

    @Test
    void create_shouldReturn201() throws Exception {
        var request = new AirportDtos.AirportCreateRequest("BOG", "Aeropuerto Internacional El Dorado", "Bogotá");
        var response = new AirportDtos.AirportResponse(1L, "BOG", "Aeropuerto Internacional El Dorado", "Bogotá");

        when(airportService.create(any(AirportDtos.AirportCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/airlines/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("BOG"))
                .andExpect(jsonPath("$.name").value("Aeropuerto Internacional El Dorado"))
                .andExpect(jsonPath("$.city").value("Bogotá"));

        verify(airportService).create(any(AirportDtos.AirportCreateRequest.class));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        var response = new AirportDtos.AirportResponse(1L, "BOG", "El Dorado International Airport", "Bogotá");

        when(airportService.get(1L)).thenReturn(response);

        mockMvc.perform(get("/api/airports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("BOG"))
                .andExpect(jsonPath("$.name").value("El Dorado International Airport"))
                .andExpect(jsonPath("$.city").value("Bogotá"));

        verify(airportService).get(1L);
    }

    @Test
    void get_shouldReturn404_whenAirportNotFound() throws Exception {
        when(airportService.get(999L)).thenThrow(new NotFoundException("Airport not found"));

        mockMvc.perform(get("/api/airports/999"))
                .andExpect(status().isNotFound());

        verify(airportService).get(999L);
    }

    @Test
    void getByCode_shouldReturn200() throws Exception {
        var response = new AirportDtos.AirportResponse(1L, "BOG", "El Dorado International Airport", "Bogotá");

        when(airportService.getByCode("BOG")).thenReturn(response);

        mockMvc.perform(get("/api/airports/by-code")
                        .param("code", "BOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("BOG"))
                .andExpect(jsonPath("$.name").value("El Dorado International Airport"))
                .andExpect(jsonPath("$.city").value("Bogotá"));

        verify(airportService).getByCode("BOG");
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var request = new AirportDtos.AirportUpdateRequest("BOG", "El Dorado Airport", "Bogotá D.C.");
        var response = new AirportDtos.AirportResponse(1L, "BOG", "El Dorado Airport", "Bogotá D.C.");

        when(airportService.update(eq(1L), any(AirportDtos.AirportUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/airports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("BOG"))
                .andExpect(jsonPath("$.name").value("El Dorado Airport"))
                .andExpect(jsonPath("$.city").value("Bogotá D.C."));

        verify(airportService).update(eq(1L), any(AirportDtos.AirportUpdateRequest.class));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/airports/1"))
                .andExpect(status().isNoContent());

        verify(airportService).delete(1L);
    }
}
