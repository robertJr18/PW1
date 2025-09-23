package com.unimag.gestion_vuelos_reservas;

import org.springframework.boot.SpringApplication;

public class TestGestionVuelosReservasApplication {

    public static void main(String[] args) {
        SpringApplication.from(GestionVuelosReservasApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
