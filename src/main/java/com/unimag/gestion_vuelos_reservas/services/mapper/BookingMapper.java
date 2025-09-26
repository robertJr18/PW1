package com.unimag.gestion_vuelos_reservas.services.mapper;

import com.unimag.gestion_vuelos_reservas.api.dto.BookingDtos;
import com.unimag.gestion_vuelos_reservas.models.Booking;
import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;
import com.unimag.gestion_vuelos_reservas.repositories.PassengerRepository;
import org.springframework.context.annotation.Profile;

public class BookingMapper {
    public static Booking toEntity(BookingDtos.BookingCreateRequest bookingCreateRequest) {
        var profile = bookingCreateRequest.passengerDto().profileDto() == null ? null :
                PassengerProfile.builder().phone(bookingCreateRequest.passengerDto().profileDto().phone())
                        .CountryCode(bookingCreateRequest.passengerDto().profileDto().countryCode()).build();


        var passenger = bookingCreateRequest.passengerDto() == null ? null:
                Passenger.builder().fullName(bookingCreateRequest.passengerDto().fullname())
                        .email(bookingCreateRequest.passengerDto().email())
                        .profile(profile).build();
        return Booking.builder().createdAt(bookingCreateRequest.createdAt())
                .passenger(passenger).build();

    }
}
