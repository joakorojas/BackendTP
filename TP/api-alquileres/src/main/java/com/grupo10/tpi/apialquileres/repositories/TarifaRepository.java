package com.grupo10.tpi.apialquileres.repositories;

import com.grupo10.tpi.apialquileres.entities.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Tarifa getTarifaByDiaSemana(int diaSemana);
    Optional<Tarifa> getTarifaByDiaMesAndMesAndAnio(int diaMes, int mes, int anio);
}
