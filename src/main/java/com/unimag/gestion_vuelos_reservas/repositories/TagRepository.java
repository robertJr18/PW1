package com.unimag.gestion_vuelos_reservas.repositories;

import com.unimag.gestion_vuelos_reservas.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<com.unimag.gestion_vuelos_reservas.models.Tag, Long> {

    // 1) Busca una etiqueta por nombre (ej: "promo", "eco", "red-eye").
    //    Retorna Optional.empty() si no existe.
    Optional<Tag> findByName(String name);

    // 2) Retorna todas las etiquetas cuyos nombres estén en la lista dada.
    //    Útil para filtros múltiples.
    List<Tag> findByNameIn(List<String> names);
}
