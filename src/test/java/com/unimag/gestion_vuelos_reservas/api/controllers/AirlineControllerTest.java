package com.unimag.gestion_vuelos_reservas.api.controllers;

import com.unimag.gestion_vuelos_reservas.api.dto.AirlineDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.services.AirlineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(AirlineController.class)
public class AirlineControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AirlineService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var request = new AirlineDtos.AirlineCreateRequest("AV", "Avianca");
        var response = new AirlineDtos.AirlineResponse(1L, "AV", "Avianca");

        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/airlines/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("AV"))
                .andExpect(jsonPath("$.name").value("Avianca"));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        when(service.get(5L)).thenReturn(new AirlineDtos.AirlineResponse(5L, "LA", "LATAM Airlines"));

        mockMvc.perform(get("/api/airlines/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.code").value("LA"))
                .andExpect(jsonPath("$.name").value("LATAM Airlines"));
    }

    @Test
    void getByCode_shouldReturn200() throws Exception {
        when(service.getByCode("AV")).thenReturn(new AirlineDtos.AirlineResponse(1L, "AV", "Avianca"));

        mockMvc.perform(get("/api/airlines/by-code")
                        .param("code", "AV"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("AV"))
                .andExpect(jsonPath("$.name").value("Avianca"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var request = new AirlineDtos.AirlineUpdateRequest("AV", "Avianca Colombia");
        var response = new AirlineDtos.AirlineResponse(2L, "AV", "Avianca Colombia");
        when(service.update(eq(2L), any(AirlineDtos.AirlineUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/airlines/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.code").value("AV"))
                .andExpect(jsonPath("$.name").value("Avianca Colombia"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/airlines/2"))
                .andExpect(status().isNoContent());
        verify(service).delete(2L);
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.get(5L)).thenThrow(new NotFoundException("Airline 5 Not Found"));

        mockMvc.perform(get("/api/airlines/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Airline 5 Not Found"));
    }
}
