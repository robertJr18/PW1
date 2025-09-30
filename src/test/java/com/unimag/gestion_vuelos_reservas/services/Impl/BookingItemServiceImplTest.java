package com.unimag.gestion_vuelos_reservas.services.Impl;


import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.*;
import com.unimag.gestion_vuelos_reservas.services.impl.BookingItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingItemServiceImplTest {
    @Mock
    private BookingItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirportRepository airportRepository;
    @Mock
    AirlineRepository airlineRepository;
    @Mock
    private SeatInventoryRepository seatInventoryRepository;

    @InjectMocks
    private BookingItemServiceImpl service;

    @Test
    void shouldCreateBookingItem() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(Booking.builder().id(1L).createdAt(OffsetDateTime.now()).build()));

        Airline airline = Airline.builder().id(23L).code("233").name("Avianca").build();
        Airport dorado = Airport.builder().id(21L).name("dorado").build();
        Airport nevado = Airport.builder().id(22L).name("nevado").build();

        when(flightRepository.findById(34L)).thenReturn(Optional.of(
                Flight.builder().id(34L).number("455A").airline(airline).origin(dorado).destination(nevado).build()
        ));

        when(itemRepository.save(any())).thenAnswer(inv -> {
            BookingItem it = inv.getArgument(0);
            it.setId(24L);
            return it;
        });

        when(seatInventoryRepository.findByFlight_IdAndCabin(34L, Cabin.BUSINESS))
                .thenReturn(Optional.of(
                        SeatInventory.builder()
                                .id(100L)
                                .flight(Flight.builder().id(34L).build())
                                .cabin(Cabin.BUSINESS)
                                .availableSeats(50)
                                .build()
                ));


        var dto = new BookingItemDtos.BookingItemCreateRequest(new BigDecimal(344), 2, Cabin.BUSINESS, 34L);
        var out = service.createBookingItem(1L, dto);

        assertThat(out.id()).isEqualTo(24L);
        assertThat(out.flightDto().id()).isEqualTo(34L);
    }

    @Test
    void shouldUpdateBookingItem() {
        var entityBooking = Booking.builder().id(2L).createdAt(OffsetDateTime.now()).build();

        Airline airline = Airline.builder().id(233L).code("233").name("Avianca").build();
        Airport dorado = Airport.builder().id(211L).name("dorado").build();
        Airport nevado = Airport.builder().id(222L).name("nevado").build();

        Flight flight = Flight.builder()
                .id(345L).number("455A").airline(airline).origin(dorado).destination(nevado)
                .build();
        when(flightRepository.findById(345L)).thenReturn(Optional.of(flight));

        var item = BookingItem.builder()
                .id(348L).booking(entityBooking).flight(flight)
                .price(new BigDecimal(344)).cabin(Cabin.PREMIUM).segmentOrder(3)
                .build();
        when(itemRepository.findById(348L)).thenReturn(Optional.of(item));

        // Mock para save -> devuelve el mismo objeto
        when(itemRepository.save(any(BookingItem.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var updateRequest = new BookingItemDtos.BookingItemUpdateRequest(
                new BigDecimal(455), 2, Cabin.BUSINESS, 345L
        );

        var out = service.updateBookingItem(348L, updateRequest);

        assertThat(out).isNotNull();
        assertThat(out.id()).isEqualTo(348L);
        assertThat(out.cabin()).isEqualTo(Cabin.BUSINESS);
        assertThat(out.price()).isEqualTo(new BigDecimal(455));

        verify(itemRepository, times(1)).findById(348L);
        verify(itemRepository, times(1)).save(any());
    }



    @Test
    void shouldReturnItemsOrderedBySegment() {
        // given
        var bookingId = 99L;
        var item1 = BookingItem.builder()
                .id(1L)
                .segmentOrder(1)
                .price(new BigDecimal("200"))
                .cabin(Cabin.ECONOMY)
                .build();
        var item2 = BookingItem.builder()
                .id(2L)
                .segmentOrder(2)
                .price(new BigDecimal("300"))
                .cabin(Cabin.BUSINESS)
                .build();

        when(itemRepository.findByBookingIdOrderBySegmentOrderAsc(bookingId))
                .thenReturn(List.of(item1, item2));

        // when
        var out = service.findItemsByBooking(bookingId);

        // then
        assertThat(out).hasSize(2);
        assertThat(out.get(0).id()).isEqualTo(1L);
        assertThat(out.get(1).id()).isEqualTo(2L);

        verify(itemRepository, times(1))
                .findByBookingIdOrderBySegmentOrderAsc(bookingId);
    }



}
