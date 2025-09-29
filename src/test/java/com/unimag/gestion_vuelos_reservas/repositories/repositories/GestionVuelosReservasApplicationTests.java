package com.unimag.gestion_vuelos_reservas.repositories.repositories;

import com.unimag.gestion_vuelos_reservas.repositories.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class GestionVuelosReservasApplicationTests {

    @Test
    void contextLoads() {
    }

}
