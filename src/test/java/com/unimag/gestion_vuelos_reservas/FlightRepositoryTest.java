package com.unimag.gestion_vuelos_reservas;

import com.unimag.gestion_vuelos_reservas.models.Airline;
import com.unimag.gestion_vuelos_reservas.models.Airport;
import com.unimag.gestion_vuelos_reservas.models.Flight;
import com.unimag.gestion_vuelos_reservas.repositories.AirlineRepository;
import com.unimag.gestion_vuelos_reservas.repositories.AirportRepository;
import com.unimag.gestion_vuelos_reservas.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;

public class FlightRepositoryTest  extends AbstractRepositoryTI {

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    AirlineRepository airlineRepository;

    @Autowired
    AirportRepository airportRepository;

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

}
