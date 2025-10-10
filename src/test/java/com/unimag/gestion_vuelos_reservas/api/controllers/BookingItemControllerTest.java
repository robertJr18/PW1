package com.unimag.gestion_vuelos_reservas.api.controllers;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.FlightDtos;
import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.services.BookingItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebMvcTest(controllers = BookingItemController.class)
public class BookingItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean

    private BookingItemService service;

    @Test
    void create_shouldReturn201andLocation() throws Exception {
        var airline= new  FlightDtos.AirlineRef(2L,"34", "dorado");
        var airport = new FlightDtos.AirportRef(1L,"455AA","MED");
        var airport2= new FlightDtos.AirportRef(2L,"455EA","BOG");

        var start = OffsetDateTime.now().plusHours(1);
        var end = start.plusHours(2);
        Set<FlightDtos.TagRef> tags = new HashSet<>();
        var flightResponse = new FlightDtos.FlightResponse(345L,"455A",start,end,airline,airport,airport2,tags);

        var request = new BookingItemDtos.BookingItemCreateRequest(new BigDecimal(322),2,Cabin.BUSINESS,345L);
        var response = new BookingItemDtos.BookingItemResponse(2L,new BigDecimal(322),2,Cabin.BUSINESS,flightResponse);
        when(service.createBookingItem(eq(1L),any())).thenReturn(response);

        mockMvc.perform(post("/api/bookings/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",org.hamcrest.Matchers.endsWith("/api/bookings/1/items/2")))
                .andExpect(jsonPath("$.id").value(2));
    }
    @Test
    void update_shouldReturn200() throws Exception {
        // Arrange (preparamos los objetos de prueba)
        var airline = new FlightDtos.AirlineRef(2L, "34", "dorado");
        var airport = new FlightDtos.AirportRef(1L, "455AA", "MED");
        var airport2 = new FlightDtos.AirportRef(2L, "455EA", "BOG");

        var start = OffsetDateTime.now().plusHours(1);
        var end = start.plusHours(2);
        Set<FlightDtos.TagRef> tags = new HashSet<>();
        var flightResponse = new FlightDtos.FlightResponse(
                345L,
                "455A",
                start,
                end,
                airline,
                airport,
                airport2,
                tags
        );

        var request = new BookingItemDtos.BookingItemCreateRequest(new BigDecimal(322),2,Cabin.BUSINESS,345L);

        var response = new BookingItemDtos.BookingItemResponse(
                2L,
                new BigDecimal("322.00"),
                2,
                Cabin.BUSINESS,
                flightResponse
        );

        when(service.updateBookingItem(eq(2L), any(BookingItemDtos.BookingItemUpdateRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/api/bookings/1/items/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.price").value(322.00))
                .andExpect(jsonPath("$.cabin").value("BUSINESS"))
                .andExpect(jsonPath("$.flightDto.number").value("455A"));
    }
    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/bookings/1/items/2"))
                .andExpect(status().isNoContent());
        verify(service).deleteBookingItem(2L);
    }
    @Test
    void list_shouldReturn200() throws Exception {
        var airline = new FlightDtos.AirlineRef(2L, "34", "dorado");
        var airport = new FlightDtos.AirportRef(1L, "455AA", "MED");
        var airport2 = new FlightDtos.AirportRef(2L, "455EA", "BOG");

        var start = OffsetDateTime.now().plusHours(1);
        var end = start.plusHours(2);
        Set<FlightDtos.TagRef> tags = new HashSet<>();
        var flightResponse = new FlightDtos.FlightResponse(
                345L,
                "455A",
                start,
                end,
                airline,
                airport,
                airport2,
                tags
        );

        var response1 = new BookingItemDtos.BookingItemResponse(
                1L,
                new BigDecimal("322.00"),
                2,
                Cabin.BUSINESS,
                flightResponse
        );
        var response2 = new BookingItemDtos.BookingItemResponse(
                2L,
                new BigDecimal("450.00"),
                2,
                Cabin.ECONOMY,
                flightResponse
        );
        when(service.findItemsByBooking(eq(99L))).thenReturn(List.of(response1,response2));
        mockMvc.perform(get("/api/bookings/99/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verificamos que hay dos elementos
                .andExpect(jsonPath("$.length()").value(2))
                // Verificamos el primero
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].price").value(322.00))
                .andExpect(jsonPath("$[0].cabin").value("BUSINESS"))
                .andExpect(jsonPath("$[0].flightDto.number").value("455A"))
                // Verificamos el segundo
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].price").value(450.00))
                .andExpect(jsonPath("$[1].cabin").value("ECONOMY"))
                .andExpect(jsonPath("$[1].flightDto.number").value("455A"));
    }

}
