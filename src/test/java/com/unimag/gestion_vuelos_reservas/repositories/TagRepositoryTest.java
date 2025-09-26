package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.Tag;
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

    void findByNameANNDfindNameinList(){
        //given
        var flight1 = Flight.builder().number("F100").origin(Airport.builder().name("dorado").build()).destination(Airport.builder().name("nevado").build()).build();
        flightRepository.saveAndFlush(flight1);
        var flight2 = Flight.builder().number("F200").origin(Airport.builder().name("josht").build()).destination(Airport.builder().name("airFonseca").build()).build();
        flightRepository.saveAndFlush(flight2);
        var tag = Tag.builder().name("dorado").flights(new HashSet<>(List.of(flight1,flight2))).build();

        tagRepository.saveAndFlush(tag);
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
