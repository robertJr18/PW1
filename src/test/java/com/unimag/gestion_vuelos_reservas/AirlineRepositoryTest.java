package com.unimag.gestion_vuelos_reservas;

import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.repositories.AirlineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class AirlineRepositoryTest extends AbstractRepositoryTI{

    @Autowired
    private AirlineRepository airlineRepository;

    @Test
    @DisplayName("Airline: Obtiene una aerolinea por código IATA")
    void ShouldFindByCode() {
        // GIVEN
        airlineRepository.save(Airline.builder().code("AV").name("Avianca").build());
        airlineRepository.save(Airline.builder().code("P5").name("Wingo").build());

        // WHEN
        var found1 = airlineRepository.findByCode("AV");
        var found2 = airlineRepository.findByCode("P5");

        // THEN
        assertThat(found1).isPresent();
        assertThat(found1.get().getName()).isEqualTo("Avianca");
        assertThat(found2.get().getName()).isEqualTo("Wingo");
    }

}
