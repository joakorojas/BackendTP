package com.grupo10.tpi.apiestaciones.Services;

import com.grupo10.tpi.apiestaciones.Entidades.Estacion;
import com.grupo10.tpi.apiestaciones.Repository.EstacionRepository;
import com.grupo10.tpi.apiestaciones.dto.EstacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstacionServiceImpl implements EstacionService{
    //inyeccion de repositorios
    private final EstacionRepository estacionRepository;

    //inyeccion de instancias de los repositorios
    @Autowired
    public EstacionServiceImpl(EstacionRepository estacionRepository) {
        this.estacionRepository = estacionRepository;
    }

    @Override
    public EstacionDTO createEstacion(EstacionDTO estacionDTO) {
        Estacion estacion = convertToEntity(estacionDTO);
        Estacion estacionCreated = estacionRepository.save(estacion);
        return convertToDto(estacionCreated);
    }

    @Override
    public EstacionDTO getEstacionById(Long id) {
       Optional<Estacion> estacion = estacionRepository.findById(id);
        if (estacion.isPresent()) {
            return estacion.map(this::convertToDto).orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public List<EstacionDTO> getAllEstaciones() {
        List<Estacion> suppliers = estacionRepository.findAll();
        return suppliers.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteEstacion(Long id) {
        estacionRepository.deleteById(id);
    }

    @Override
    public EstacionDTO updateEstacion(Long id, EstacionDTO estacionDTO) {
        Estacion estacion = convertToEntity(estacionDTO);
        if (estacionRepository.existsById(id)) {
            estacion.setId(id);
            estacionRepository.save(estacion);
            return convertToDto(estacion);
        } else {
            throw new RuntimeException("Estacion not found");
        }
    }

    @Override
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        // Conversión de grados a metros (asumiendo que 1 grado = 110,000 metros)
        double LatitudMetros = Math.abs(lat1 - lat2) * 110;
        double LongitudMetros = Math.abs(lon1 - lon2) * 110;

        // Distancia euclídea
        return Math.sqrt(Math.pow(LatitudMetros, 2) + Math.pow(LongitudMetros, 2));

    }

    @Override
    public Optional<EstacionDTO> getEstacionCercana(double latitud, double longitud) {
        //devolver optional empty si no se encuentra una estacion cercana
         Optional<Estacion> estacionOptional = estacionRepository.findAll().stream()
                .min((estacion1, estacion2) -> {
                    double distancia1 = calcularDistancia(estacion1.getLatitud(), estacion1.getLongitud(), latitud, longitud);
                    double distancia2 = calcularDistancia(estacion2.getLatitud(), estacion2.getLongitud(), latitud, longitud);
                    return Double.compare(distancia1, distancia2);
                });
        return estacionOptional.map(this::convertToDto);

    }

    public EstacionDTO convertToDto(Estacion estacion) {
        EstacionDTO estacionDTO = new EstacionDTO();
        estacionDTO.setId(estacion.getId());
        estacionDTO.setNombre(estacion.getNombre());
        estacionDTO.setLatitud(estacion.getLatitud());
        estacionDTO.setLongitud(estacion.getLongitud());

        return estacionDTO;
    }

    public Estacion convertToEntity(EstacionDTO estacionDTO) {
        if (estacionDTO == null) {
            return null;
        }
        Estacion estacion = new Estacion();
        estacion.setId(estacionDTO.getId());
        estacion.setNombre(estacionDTO.getNombre());
        estacion.setCreacionDate(LocalDateTime.now());
        estacion.setLatitud(estacionDTO.getLatitud());
        estacion.setLongitud(estacionDTO.getLongitud());

        return estacion;
    }



}




