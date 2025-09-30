package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Airport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

public class AirportRepositoryTest extends AbstractRepositoryTI {

    @Autowired
    AirportRepository airportRepository;

    @Test
    @DisplayName("Airport: encuentra por código IATA")
    void shouldFindByCode() {
        // GIVEN
        airportRepository.save(Airport.builder().code("BOG").name("El Dorado").city("Bogotá").build());
        airportRepository.save(Airport.builder().code("MDE").name("José María Córdova").city("Rionegro").build());

        // WHEN
        Optional<Airport> found = airportRepository.findByCode("BOG");
        Optional<Airport> notFound = airportRepository.findByCode("XXX");

        // THEN
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("El Dorado");
        assertThat(notFound).isEmpty();
    }
}
