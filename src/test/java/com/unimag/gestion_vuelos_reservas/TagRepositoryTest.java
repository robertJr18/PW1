package com.unimag.gestion_vuelos_reservas;

import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.Tag;
import com.unimag.gestion_vuelos_reservas.repositories.AirlineRepository;
import com.unimag.gestion_vuelos_reservas.repositories.AirportRepository;
import com.unimag.gestion_vuelos_reservas.repositories.FlightRepository;
import com.unimag.gestion_vuelos_reservas.repositories.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class TagRepositoryTest extends AbstractRepositoryTI {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private AirlineRepository airlineRepository;

    @Test
    void findByNameANDfindNameinList(){
        //given
        var flight1 = Flight.builder().number("F100").origin(airportRepository.save(Airport.builder().name("dorado").build())).destination(airportRepository.save(Airport.builder().name("nevado").build())).airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAO").build())).build();
        flightRepository.save(flight1);
        var flight2 = Flight.builder().number("F200").origin(airportRepository.save(Airport.builder().name("josht").build())).destination(airportRepository.save(Airport.builder().name("airFonseca").build())).airline(airlineRepository.save(Airline.builder().name("American Airlines").code("AAE").build())).build();
        flightRepository.save(flight2);

        var tag = Tag.builder().name("dorado").flights(new HashSet<>(List.of(flight1,flight2))).build();

        tagRepository.save(tag);
        // when
        Optional<Tag> findByName = tagRepository.findByName(tag.getName());

        //then
        assertThat(findByName).isPresent();
        assertThat(findByName.get().getFlights()).hasSize(2);
    }

    @Test
    @DisplayName("Debe encontrar múltiples etiquetas por lista de nombres")
    void testFindByNameIn() {
        // Arrange
        Tag t1 = Tag.builder().name("promo").build();
        Tag t2 = Tag.builder().name("dorado").build();
        Tag t3 = Tag.builder().name("luxury").build();
        tagRepository.saveAll(List.of(t1, t2, t3));

        // Act
        List<Tag> found = tagRepository.findByNameIn(List.of("promo", "luxury"));

        // Assert
        assertThat(found).hasSize(2);
        assertThat(found)
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("promo", "luxury");
    }

}
