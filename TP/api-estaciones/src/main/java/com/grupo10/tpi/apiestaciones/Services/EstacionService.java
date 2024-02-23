package com.grupo10.tpi.apiestaciones.Services;

import com.grupo10.tpi.apiestaciones.Entidades.Estacion;
import com.grupo10.tpi.apiestaciones.dto.EstacionDTO;

import java.util.List;
import java.util.Optional;

public interface EstacionService {
    EstacionDTO createEstacion(EstacionDTO estacion);

    EstacionDTO getEstacionById(Long id);

    List<EstacionDTO> getAllEstaciones();

    void deleteEstacion(Long id);

    EstacionDTO updateEstacion(Long id, EstacionDTO estacion);

    double calcularDistancia(double lat1, double lon1, double lat2, double lon2);

    Optional<EstacionDTO> getEstacionCercana(double latitud, double longitud);
}