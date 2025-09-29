package com.unimag.gestion_vuelos_reservas.services.impl;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.Booking;
import com.unimag.gestion_vuelos_reservas.models.BookingItem;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.SeatInventory;
import com.unimag.gestion_vuelos_reservas.repositories.BookingItemRepository;
import com.unimag.gestion_vuelos_reservas.repositories.BookingRepository;
import com.unimag.gestion_vuelos_reservas.repositories.FlightRepository;
import com.unimag.gestion_vuelos_reservas.repositories.SeatInventoryRepository;
import com.unimag.gestion_vuelos_reservas.services.BookingItemService;
import com.unimag.gestion_vuelos_reservas.services.mapper.BokingItemMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service@Transactional@RequiredArgsConstructor
public class BookingItemServiceImpl  implements BookingItemService {

    private final BookingItemRepository bookingItemRepository;
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final BokingItemMapper bokingItemMapper;

    @Override
    public BookingItemDtos.BookingItemResponse createBookingItem(Long bookingId, BookingItemDtos.BookingItemCreateRequest request) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));

        Flight flight = flightRepository.findById(request.flighId()).orElseThrow(() -> new NotFoundException("Flight not found"));

        //reservando asiento
        SeatInventory inventory = seatInventoryRepository.findByFlight_IdAndCabin(flight.getId(), request.cabin()).orElseThrow(() -> new NotFoundException("Seat inventory not found"));
        if (inventory.getAvailableSeats() <1){
            throw new IllegalStateException("Not enough seats available");
        }
        inventory.setAvailableSeats(inventory.getAvailableSeats() - 1);
        seatInventoryRepository.save(inventory);

        //creando bookingItem
        BookingItem bookingItem = BokingItemMapper.toEntity(request,flight);
        bookingItem.setBooking(booking);

        BookingItem bookingItemSaved = bookingItemRepository.save(bookingItem);
        return  bokingItemMapper.toResponse(bookingItemSaved);

    }

    @Override
    public BookingItemDtos.BookingItemResponse updateBookingItem(BookingItemDtos.BookingItemUpdateRequest request) {

        BookingItem bookingItem = bookingItemRepository.findById(request.id()).orElseThrow(() -> new NotFoundException("Booking not found"));

        Flight flight = null;
        if (request != null){
        flight= flightRepository.findById(request.flight_id()).orElseThrow(() -> new NotFoundException("Flight not found"));}

        BokingItemMapper.updateEntity(bookingItem,request,flight);
        BookingItem bookingItemUpdate = bookingItemRepository.save(bookingItem);

        return BokingItemMapper.toResponse(bookingItemUpdate);
    }

    @Override
    public BookingItemDtos.BookingItemResponse deleteBookingItem(long id) {
        BookingItem bookingItem =  bookingItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Booking not found"));

        SeatInventory inventory = seatInventoryRepository.findByFlight_IdAndCabin(bookingItem.getFlight().getId(), bookingItem.getCabin()).orElseThrow(() -> new NotFoundException("Seat inventory not found"));

        inventory.setAvailableSeats(inventory.getAvailableSeats() - 1);
        seatInventoryRepository.save(inventory);

        bookingItemRepository.delete(bookingItem);

        return BokingItemMapper.toResponse(bookingItem);
    }

    @Override
    public List<BookingItemDtos.BookingItemResponse> findItemsByBooking(Long bookingId) {
        List<BookingItem> bookingItems = bookingItemRepository.findByBookingIdOrderBySegmentOrderAsc(bookingId);
        if (bookingItems.isEmpty()){
            throw new NotFoundException("Not items found for this booking");
        }
        return bookingItems.stream().map(BokingItemMapper::toResponse).toList();
    }
}
