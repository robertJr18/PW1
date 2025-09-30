package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.models.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class FlightRepositoryTest  extends AbstractRepositoryTI {

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    AirlineRepository airlineRepository;

    @Autowired
    AirportRepository airportRepository;
    @Autowired
    private TagRepository tagRepository;

    // -------- Helpers --------
    private Airline createAirline(String code, String name) {
        return airlineRepository.save(Airline.builder().code(code).name(name).build());
    }

    private Airport createAirport(String code, String name, String city) {
        return airportRepository.save(Airport.builder().code(code).name(name).city(city).build());
    }

    private Flight createFlight(String number, Airline al, Airport origin, Airport dest,
                                OffsetDateTime dep, OffsetDateTime arr) {
        return flightRepository.save(
                Flight.builder()
                        .number(number)
                        .airline(al)
                        .origin(origin)
                        .destination(dest)
                        .departureTime(dep)
                        .arrivalTime(arr)
                        .build()
        );
    }

    private Tag createTag(String name) {
        return tagRepository.save(Tag.builder().name(name).build());
    }

    // 1) Obtener lista de vuelos operados por una aerolinea
    @Test
    @DisplayName("Obtener lista de vuelos operados por una aerolinea")
    void shouldReturnFlightsByAirlineNameWithPagination() {
        // GIVEN
        var avianca = createAirline("AV", "Avianca");
        var latam = createAirline("LA", "LATAM");
        var bog = createAirport("BOG", "El Dorado", "Bogotá");
        var mde = createAirport("MDE", "JMC", "Rionegro");

        var t0 = OffsetDateTime.parse("2025-01-01T08:00:00Z");
        createFlight("AV100", avianca, bog, mde, t0, t0.plusHours(1));
        createFlight("AV200", avianca, bog, mde, t0.plusHours(2), t0.plusHours(3));
        createFlight("AV300", avianca, bog, mde, t0.plusHours(4), t0.plusHours(5));
        createFlight("LA900", latam, bog, mde, t0.plusHours(6), t0.plusHours(7));

        // WHEN
        Pageable p0 = PageRequest.of(0, 2, Sort.by("number").ascending());
        Pageable p1 = PageRequest.of(1, 2, Sort.by("number").ascending());
        Page<Flight> first = flightRepository.findByAirline_Name("Avianca", p0);
        Page<Flight> second = flightRepository.findByAirline_Name("Avianca", p1);

        // THEN
        assertThat(first.getTotalElements()).isEqualTo(3);
        assertThat(first.getTotalPages()).isEqualTo(2);
        assertThat(first.getContent()).hasSize(2);
        assertThat(second.getContent()).hasSize(1);
        assertThat(first.getContent())
                .allMatch(f -> f.getAirline().getName().equals("Avianca"));
        assertThat(second.getContent())
                .allMatch(f -> f.getAirline().getName().equals("Avianca"));
    }

    // 2) Busca vuelos por origen, destino y ventana de salida
    @Test
    @DisplayName("Busca vuelos por origen, destino y ventana de salida")
    void shouldReturnFlightsByRouteAndRangeIncludingBoundaries() {
        // GIVEN
        var avianca = createAirline("AV", "Avianca");
        var bog = createAirport("BOG", "El Dorado", "Bogotá");
        var mad = createAirport("MAD", "Barajas", "Madrid");

        var from = OffsetDateTime.parse("2025-02-01T08:00:00Z");
        var to = OffsetDateTime.parse("2025-02-01T12:00:00Z");

        createFlight("AV101", avianca, bog, mad, from, from.plusHours(10)); // borde inferior
        createFlight("AV102", avianca, bog, mad, from.plusHours(2), from.plusHours(12)); // dentro
        createFlight("AV103", avianca, bog, mad, to, to.plusHours(9));    // borde superior

        createFlight("AV999", avianca, bog, mad, to.plusHours(1), to.plusHours(8));

        // WHEN
        Pageable p0 = PageRequest.of(0, 2, Sort.by("number"));
        Pageable p1 = PageRequest.of(1, 2, Sort.by("number"));
        Page<Flight> page0 = flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
                "BOG", "MAD", from, to, p0);
        Page<Flight> page1 = flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
                "BOG", "MAD", from, to, p1);

        // THEN
        assertThat(page0.getTotalElements()).isEqualTo(3);
        assertThat(page0.getTotalPages()).isEqualTo(2);
        assertThat(page0.getContent()).hasSize(2);
        assertThat(page1.getContent()).hasSize(1);
        assertThat(page0.getContent())
                .allMatch(f -> f.getOrigin().getCode().equals("BOG") && f.getDestination().getCode().equals("MAD"));
        assertThat(page1.getContent())
                .allMatch(f -> f.getOrigin().getCode().equals("BOG") && f.getDestination().getCode().equals("MAD"));
    }

    // 3) Filtra por origen/destino y ventana de salida
    @Test
    @DisplayName("Filtra por origen/destino y ventana de salida")
    void shouldFilterFlightsWithOptionalOriginAndDestination() {
        // GIVEN
        var avianca = createAirline("AV", "Avianca");
        var bog = createAirport("BOG", "El Dorado", "Bogotá");
        var mad = createAirport("MAD", "Barajas", "Madrid");
        var mde = createAirport("MDE", "JMC", "Rionegro");

        var from = OffsetDateTime.parse("2025-04-01T08:00:00Z");
        var to = OffsetDateTime.parse("2025-04-01T12:00:00Z");

        createFlight("AV300", avianca, bog, mad, from.plusHours(1), to.plusHours(2)); // cumple
        createFlight("AV301", avianca, bog, mde, from.plusHours(1), to.plusHours(2)); // destino no coincide
        createFlight("AV302", avianca, mde, mad, from.plusHours(1), to.plusHours(2)); // origen no coincide
        createFlight("AV303", avianca, bog, mad, to.plusHours(1), to.plusHours(5)); // fuera de rango

        // WHEN
        var results = flightRepository.searchWithAssociations("BOG", "MAD", from, to);

        // THEN
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getNumber()).isEqualTo("AV300");
        assertThat(results.get(0).getAirline().getName()).isEqualTo("Avianca");
    }

    // 4) Devuelve los vuelos que poseen todas las tags indicadas
    @Test
    @DisplayName("Devuelve los vuelos que poseen todas las tags indicadas")
    void shouldReturnFlightsWithAllTags() {
        // GIVEN
        var avianca = createAirline("AV", "Avianca");
        var bog = createAirport("BOG", "El Dorado", "Bogotá");
        var mad = createAirport("MAD", "Barajas", "Madrid");

        var dep = OffsetDateTime.parse("2025-05-01T08:00:00Z");
        var arr = dep.plusHours(10);

        var flight1 = createFlight("AV400", avianca, bog, mad, dep, arr);
        var flight2 = createFlight("AV401", avianca, bog, mad, dep, arr);

        var t1 = createTag("International");
        var t2 = createTag("Night");

        t1.addFlight(flight1);
        t1.addFlight(flight2);
        t2.addFlight(flight1);

        tagRepository.save(t1);
        tagRepository.save(t2);

        // WHEN
        var results = flightRepository.findFlightsWithAllTags(
                java.util.List.of("International", "Night"),
                2
        );

        // THEN
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getNumber()).isEqualTo("AV400");
    }
}
