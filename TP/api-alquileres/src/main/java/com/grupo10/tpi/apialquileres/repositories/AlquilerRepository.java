package com.grupo10.tpi.apialquileres.repositories;

import com.grupo10.tpi.apialquileres.entities.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
}
