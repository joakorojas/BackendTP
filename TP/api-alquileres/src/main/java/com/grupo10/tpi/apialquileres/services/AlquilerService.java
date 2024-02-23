package com.grupo10.tpi.apialquileres.services;


import com.grupo10.tpi.apialquileres.dtos.EstacionDTO;
import com.grupo10.tpi.apialquileres.entities.Alquiler;

import java.util.List;
import java.util.Optional;

public interface AlquilerService {
    Alquiler createAlquiler(Alquiler alquiler);

    Alquiler getAlquilerById(Long id);

    List<Alquiler> getAllAlquilers();

    void deleteAlquiler(Long id);

    Alquiler updateAlquiler(Long id, Alquiler alquiler);

    //punto3
    Alquiler iniciarAlquiler(String clienteId, Long estacionRetiroId);

    //punto4
    Alquiler finalizarAlquiler(Long idAlquiler, Long estacionDevolucionId, String moneda);

    List<EstacionDTO> traerEstaciones();

    List<Alquiler>getAllAlquilersWithFilter(String clienteId);

}
