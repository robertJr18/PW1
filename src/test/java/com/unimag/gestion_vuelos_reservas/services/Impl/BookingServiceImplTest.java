package com.unimag.gestion_vuelos_reservas.services.Impl;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.BookingRepository;
import com.unimag.gestion_vuelos_reservas.repositories.FlightRepository;
import com.unimag.gestion_vuelos_reservas.repositories.PassengerRepository;
import com.unimag.gestion_vuelos_reservas.repositories.SeatInventoryRepository;
import com.unimag.gestion_vuelos_reservas.services.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private SeatInventoryRepository  seatInventoryRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Test
    void shouldCreateBooking(){
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(Passenger.builder().id(1L).fullName("Joselo").email("jo@gmail.com").build()));
        when(bookingRepository.save(any())).thenAnswer(i -> {
            Booking booking = i.getArgument(0); booking.setId(11L); return booking;
        });
        Airline airline = Airline.builder().id(23L).code("233").name("Avianca").build();
        Airport dorado = Airport.builder().id(21L).name("dorado").build();
        Airport nevado = Airport.builder().id(22L).name("nevado").build();

        var flight =  Flight.builder().id(34L).number("455A").airline(airline).origin(dorado).destination(nevado).build();
        when(flightRepository.findAllById(any())).thenAnswer(invocation -> {
            List<Long> ids = invocation.getArgument(0);
            return ids.stream()
                    .map(id -> {
                        if (id.equals(34L)) return flight;
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
        });


        when(seatInventoryRepository.decrementAvailableSeats(anyLong(), any(), anyInt()))
                .thenReturn(1);
        
        List<BookingItemDtos.BookingItemCreateRequest> bookingItems = List.of(
                new BookingItemDtos.BookingItemCreateRequest(new BigDecimal(344), 2, Cabin.BUSINESS, 34L));

        var bookingResponse = bookingService.createBooking(new BookingDtos.BookingCreateRequest(OffsetDateTime.now(),1L,bookingItems));

        assertThat(bookingResponse).isNotNull();
        assertThat(bookingResponse.id()).isEqualTo(11L);
        assertThat(bookingResponse.passengerDto().fullname()).isEqualTo("Joselo");

    }
    @Test
    void shouldUpdateBooking() {
        // given
        Passenger passenger = Passenger.builder()
                .id(1L)
                .fullName("Joselo")
                .email("jo@gmail.com")
                .build();

        Flight flight = Flight.builder()
                .id(34L)
                .number("455A")
                .build();

        Booking existingBooking = Booking.builder()
                .id(11L)
                .passenger(passenger)
                .items(new ArrayList<>())
                .createdAt(OffsetDateTime.now().minusDays(1))
                .build();

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        when(bookingRepository.findById(11L)).thenReturn(Optional.of(existingBooking));

        when(bookingRepository.save(any())).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            return b;
        });

        // Construir request con cambios
        List<BookingItemDtos.BookingItemUpdateRequest> updatedItems = List.of(
                new BookingItemDtos.BookingItemUpdateRequest( new BigDecimal("500"), 2, Cabin.BUSINESS, 34L)
        );

        BookingDtos.BookingUpdateRequest updateRequest = new BookingDtos.BookingUpdateRequest(
                1L
        );

        // Act
        BookingDtos.BookingResponse response = bookingService.updateBooking(11L, updateRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(11L);
        assertThat(response.passengerDto().fullname()).isEqualTo("Joselo");
    }

    @Test
    void shouldFindBookingsByPassengerEmail() {
        // Arrange
        Passenger passenger = Passenger.builder()
                .id(1L)
                .fullName("Joselo")
                .email("jo@gmail.com")
                .build();

        Booking booking1 = Booking.builder()
                .id(11L)
                .passenger(passenger)
                .createdAt(OffsetDateTime.now())
                .items(new ArrayList<>())
                .build();

        Booking booking2 = Booking.builder()
                .id(12L)
                .passenger(passenger)
                .createdAt(OffsetDateTime.now())
                .items(new ArrayList<>())
                .build();

        // Mock: cuando el repositorio busque por email, devuelve estos bookings
        when(bookingRepository.findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc(
                "jo@gmail.com", PageRequest.ofSize(10)))
                .thenReturn(new PageImpl<>(List.of(booking1, booking2)));

        // Act
        List<BookingDtos.BookingResponse> responses =
                bookingService.finBookingByPassengerEmail("jo@gmail.com");

        // Assert
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(11L);
        assertThat(responses.get(1).id()).isEqualTo(12L);
        assertThat(responses.get(0).passengerDto().email()).isEqualTo("jo@gmail.com");
    }

}
