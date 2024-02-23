package com.grupo10.tpi.apialquileres.services;

import com.grupo10.tpi.apialquileres.entities.Alquiler;
import com.grupo10.tpi.apialquileres.entities.Tarifa;

import java.util.List;
import java.util.Optional;

public interface TarifaService {
    Tarifa createTarifa(Tarifa tarifa);

    Optional<Tarifa> getTarifaById(Long id);

    List<Tarifa> getAllTarifas();

    void deleteTarifa(Long id);

    Tarifa updateTarifa(Long id, Tarifa tarifa);

    public Tarifa getTarifa();
    double calcularTarifa(Alquiler alquiler, Tarifa tarifa, Long estcionDevolucionId);
}
