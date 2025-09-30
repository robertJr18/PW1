package com.unimag.gestion_vuelos_reservas.services.Impl;

import com.unimag.gestion_vuelos_reservas.api.dto.PassengerDtos;
import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;
import com.unimag.gestion_vuelos_reservas.repositories.PassengerRepository;
import com.unimag.gestion_vuelos_reservas.services.impl.PassengerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceImplTest {
    @Mock
    PassengerRepository repository;
    @InjectMocks
    PassengerServiceImpl service;

    @Test
    void shouldCreatePassengerAndReturnPassengerResponse() {
        var request = new PassengerDtos.PassengerCreateRequest("Jose Rodriguez","joselitBorrachon@gmail.com",new PassengerDtos.PassengerProfileDto("300554","57"));
        when(repository.save(any(Passenger.class))).thenAnswer(inv -> {
            Passenger p = inv.getArgument(0);
            p.setId(11L);
            return p;
        });

        var response= service.createPassenger(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(11L);
        assertThat(response.email()).isEqualTo("joselitBorrachon@gmail.com");
        verify(repository).save(any(Passenger.class));
    }

    @Test
    void shouldUpdatePassengerAndReturnPassengerResponse() {
        var entity = Passenger.builder().id(8L).fullName("Jose").email("alejo@gmail.com")
                .profile(PassengerProfile.builder().countryCode("1").phone("123456789").build()).build();
        when(repository.findById(8L)).thenReturn(Optional.of(entity));
        when(repository.save(any(Passenger.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var passengerUpdateRequest = new PassengerDtos.PassengerUpdateRequest("Joselito", "alejo01@gmail.com",new PassengerDtos.PassengerProfileDto("300554","57"));

        var passengerUpdateResponse= service.updatePassenger(8L,passengerUpdateRequest);

        assertThat(passengerUpdateResponse.fullname()).isEqualTo("Joselito");
        assertThat(passengerUpdateResponse.profileDto().countryCode()).isEqualTo("57");
    }

}
