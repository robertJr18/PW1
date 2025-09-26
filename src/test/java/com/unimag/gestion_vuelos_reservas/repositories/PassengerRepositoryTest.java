package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Passenger;
import com.unimag.gestion_vuelos_reservas.models.PassengerProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static  org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

public class PassengerRepositoryTest extends AbstractRepositoryTI {



    @Autowired
    private PassengerRepository passengerRepository;



    @Test
    @DisplayName("Passenger: encuentra por email(ignore case)")
    void shouldFindByEmailIgnoreCase() {
        //Given
        var pofile= PassengerProfile.builder() .phone("123456789").CountryCode("57") .build();

        var passenger= Passenger.builder().email("JOSE@DEMO.COM"). fullName("Jose Rodriguez").profile(pofile).build();
        passengerRepository.saveAndFlush(passenger);

        //when
         Optional<Passenger> byEmail= passengerRepository. findByEmailIgnoreCase("JOSE@DEMO.COM");

         //Then
         assertThat(byEmail.isPresent()).isTrue();
         assertThat(byEmail.get().getEmail()).isEqualTo("JOSE@DEMO.COM");
    }

    void findByEmailWithProfileIgnoreCase(){
        //Given


        var profile = PassengerProfile.builder().phone("123456789").CountryCode("57").build();

        var passenger = Passenger.builder().email("JOSE@DEMO.COM"). fullName("Jose Rodriguez").profile(profile).build();
        passengerRepository.saveAndFlush(passenger);


        Optional<Passenger> result = passengerRepository.findByEmailWithProfileIgnoreCase("jose@demo.com");

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getFullName()).isEqualTo("Jose Rodriguez");
        assertThat(result.get().getProfile().getCountryCode()).isEqualTo("57");
        assertThat(result.get().getProfile().getPhone()).isEqualTo("123456789");
    }

}
