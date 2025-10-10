package com.unimag.gestion_vuelos_reservas.services.impl;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;
import com.unimag.gestion_vuelos_reservas.api.dto.BookingItemDtos;
import com.unimag.gestion_vuelos_reservas.exception.NotFoundException;
import com.unimag.gestion_vuelos_reservas.models.*;
import com.unimag.gestion_vuelos_reservas.repositories.*;
import com.unimag.gestion_vuelos_reservas.services.BookingItemService;
import com.unimag.gestion_vuelos_reservas.services.BookingService;
import com.unimag.gestion_vuelos_reservas.services.mapper.BokingItemMapper;
import com.unimag.gestion_vuelos_reservas.services.mapper.BookingMapper;
import com.unimag.gestion_vuelos_reservas.services.mapper.SeatInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.awt.print.Pageable;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final BookingItemRepository bookingItemRepository;
    private final BookingItemService bookingItemService;
    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final SeatInventoryMapper  seatInventoryMapper;

    /**
     * crea una reserva con todas las validaciones:
     * 1. el pasajero debe existir
     * 2. todos los vuelos deben existir
     * 3. loa vuelos deben haber despegado
     * 4. debe haber asientos disponibles en la cabina que se quiere
     * 5. el orden de los segmentos debe ser valido
     * 6. no se pueden duplicar vuelos en la misma reserva
     */
    @Override
    public BookingDtos.BookingResponse createBooking(BookingDtos.BookingCreateRequest request) {
        //validaciones basicas
        validarCreateBookingRequest(request);
        //pasajeros
        Passenger passenger = resolvePassenger(request.passengerId());
        //vuelos
        List<Flight> flights = resolveandvalidateFlight(request.items());

        try {
            attemptAtomicReserveSeats(request.items());
        } catch (RuntimeException ex) {throw ex;}

        List<BookingItem> items = createBookinItems(request.items(),flights);
        Booking booking = BookingMapper.toEntity(request,passenger,items);

        // 7. Asegurar bidireccionalidad: cada item debe apuntar al booking y booking.items debe contenerlos
        if (booking.getItems() != null) {
            for (BookingItem it : booking.getItems()) {
                it.setBooking(booking);
            }
        } else {
            booking.setItems(new ArrayList<>(items));
            for (BookingItem it : booking.getItems()) {
                it.setBooking(booking);
            }
        }

        Booking bookingSaved = bookingRepository.save(booking);
            
        return BookingMapper.toResponse(bookingSaved);

    }

    /**
     * Intento atómico de reservar asientos: para cada item ejecuto un UPDATE que decrementa
     * availableSeats solo si hay suficientes. Si alguna actualización devuelve 0 -> fail.
     */
    private void attemptAtomicReserveSeats(List<BookingItemDtos.BookingItemCreateRequest> items) {
        // Agrupar por flight+cabin para optimizar (si hay duplicados en items)
        Map<String, Integer> needed = new HashMap<>();
        for (BookingItemDtos.BookingItemCreateRequest it : items) {
            String key = it.flighId() + "-" + it.cabin().name();
            needed.put(key, needed.getOrDefault(key, 0) + 1);
        }

        // Ejecutar decrementos atómicos por cada key
        for (Map.Entry<String, Integer> e : needed.entrySet()) {
            String[] parts = e.getKey().split("-");
            Long flightId = Long.valueOf(parts[0]);
            Cabin cabin = Cabin.valueOf(parts[1]);
            int qty = e.getValue();

            int updated = seatInventoryRepository.decrementAvailableSeats(flightId, cabin, qty);
            if (updated <= 0) {
                // No hay suficientes asientos: revertir es responsabilidad de la transacción (rollback)
                throw new IllegalStateException("Not enough seats available for flight " + flightId + " cabin " + cabin);
            }
        }
    }

    // actualizar una reserva
    @Override
    public BookingDtos.BookingResponse updateBooking(Long id,BookingDtos.BookingUpdateRequest request) {
        if (request == null) throw new IllegalArgumentException("BookingUpdateRequest can't be null!");
        Booking booking = bookingRepository.findById(id).orElseThrow(()-> new NotFoundException("Booking Not Found"));

        validateBookingCanBeModified(booking);

        Passenger passenger = null;
        if (request.passenger_id() != null){
            passenger =passengerRepository.findById(request.passenger_id()).orElseThrow(()-> new NotFoundException("Passenger Not Found"));
        }
        BookingMapper.updateEntity(booking,passenger);
        Booking updateBooking = bookingRepository.save(booking);
        return BookingMapper.toResponse(updateBooking);
    }

    @Override
    public List<BookingDtos.BookingResponse> finBookingByPassengerEmail(  String email) {
        if (email.trim().isBlank()) throw new IllegalArgumentException("BookingCreateRequest passengerEmail can't be null!");
        Page<Booking> page = bookingRepository.findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc(email,PageRequest.ofSize(10));
        List<Booking> bookings = page.getContent();

        return bookings.stream().map(BookingMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public BookingDtos.BookingResponse getBookingWithDetails(long id) {
        return bookingRepository.searchWithAllDetails(id).map(BookingMapper::toResponse)
                .orElseThrow(()-> new NotFoundException("Booking Not Found"));
    }

    @Override@Transactional(readOnly = true)
    public BookingDtos.BookingResponse getBookingId(long id) {
        Booking booking = bookingRepository.searchWithAllDetails(id).orElseThrow(()-> new NotFoundException("Booking Not Found"));
        return BookingMapper.toResponse(booking);
    }

    @Override
    public void deleteById(long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(()-> new NotFoundException("Booking Not Found"));
        releaseSeatsFromBooking(booking);
        bookingRepository.delete(booking);
    }
    private List<BookingItem> createBookinItems(List<BookingItemDtos.BookingItemCreateRequest> items, List<Flight> flights) {
        // Crear mapa de vuelos por ID
        Map<Long, Flight> flightMap = flights.stream()
                .collect(Collectors.toMap(Flight::getId, flight -> flight));

        return items.stream()
                .map(item -> {
                    Flight flight = flightMap.get(item.flighId());
                    if (flight == null) {
                        throw new IllegalArgumentException("No flight found for id " + item.flighId());
                    }
                    return BokingItemMapper.toEntity(item, flight);
                })
                .toList();
    }

    private void confirmSeatReservations(List<BookingItemDtos.BookingItemCreateRequest> items) {
        for (BookingItemDtos.BookingItemCreateRequest item : items) {
            SeatInventory seatInventory = seatInventoryRepository.findByFlight_IdAndCabin(item.flighId(), item.cabin())
                    .orElseThrow(()-> new NotFoundException("Seat Inventory Not Found"));

            seatInventory.setAvailableSeats(seatInventory.getAvailableSeats() - 1);
            seatInventoryRepository.save(seatInventory);

        }
    }


    private List<Flight> resolveandvalidateFlight(List<BookingItemDtos.BookingItemCreateRequest> items) {
        List<Long> requestedIds = items.stream()
                .map(BookingItemDtos.BookingItemCreateRequest::flighId)
                .toList();

        List<Flight> flights = flightRepository.findAllById(requestedIds);

        List<Long> foundIds = flights.stream().map(Flight::getId).toList();

        List<Long> notFound = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!notFound.isEmpty()) {
            throw new NotFoundException("No flights found with ids " + notFound);
        }

        return flights;
    }

    private Passenger resolvePassenger(Long id) {
        return passengerRepository.findById(id).orElseThrow(()-> new NotFoundException("Passenger Not found"));
    }

    private void validarCreateBookingRequest(BookingDtos.BookingCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("BookingCreateRequest can't be null!");
        if (CollectionUtils.isEmpty(request.items())) throw new IllegalArgumentException("BookingCreateRequest items can't be null!");
        if (request.createdAt() == null) throw new IllegalArgumentException("BookingCreateRequest createdAt can't be null!");
    }

    private void validateBookingCanBeModified(Booking booking) {
        if (booking == null || booking.getItems() == null) {
            return;
        }

        // Verificar si algún vuelo ya despegó
        List<BookingItem> departedItems = booking.getItems().stream()
                .filter(item -> item.getFlight() != null &&
                        item.getFlight().getDepartureTime() != null &&
                        item.getFlight().getDepartureTime().isBefore(OffsetDateTime.now()))
                .toList();

        if (!departedItems.isEmpty()) {
            throw new NotFoundException("Departure Time can't be modified!, some flights have already taken off");
        }
    }

    private void releaseSeatsFromBooking(Booking booking) {
        if (booking == null || booking.getItems() == null) return;

        // Agrupar por flight+cabin para hacer increments en batch
        Map<String, Integer> toRelease = new HashMap<>();
        for (BookingItem bookingItem : booking.getItems()) {
            Long flightId = bookingItem.getFlight().getId();
            Cabin cabin = bookingItem.getCabin();
            String key = flightId + "-" + cabin.name();
            toRelease.put(key, toRelease.getOrDefault(key, 0) + 1);
        }

        for (Map.Entry<String, Integer> e : toRelease.entrySet()) {
            String[] parts = e.getKey().split("-");
            Long flightId = Long.valueOf(parts[0]);
            Cabin cabin = Cabin.valueOf(parts[1]);
            int qty = e.getValue();

            try {
                seatInventoryRepository.incrementAvailableSeats(flightId, cabin, qty);
            } catch (Exception ex) {
                // No propagar: registrar y continuar (mejor usar logger real)
                System.err.println("Error releasing seats for flight " + flightId + " cabin " + cabin + ": " + ex.getMessage());
            }
        }
    }
}
