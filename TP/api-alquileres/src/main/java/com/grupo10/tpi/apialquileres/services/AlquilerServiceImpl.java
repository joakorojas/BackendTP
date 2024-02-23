package com.grupo10.tpi.apialquileres.services;

import com.grupo10.tpi.apialquileres.dtos.EstacionDTO;
import com.grupo10.tpi.apialquileres.entities.Alquiler;
import com.grupo10.tpi.apialquileres.entities.Tarifa;
import com.grupo10.tpi.apialquileres.repositories.AlquilerRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate.*;
import com.grupo10.tpi.apialquileres.services.MonedaService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlquilerServiceImpl implements AlquilerService{

    private TarifaService tarifaService;
    private final AlquilerRepository alquilerRepository;

    private MonedaService monedaService;


    //inyeccion de instancias de los repositorios
    @Autowired
    public AlquilerServiceImpl(TarifaService tarifaService, AlquilerRepository alquilerRepository, MonedaService monedaService) {
        this.tarifaService = tarifaService;
        this.alquilerRepository = alquilerRepository;
        this.monedaService = monedaService;
    }

    @Override
    public Alquiler createAlquiler(Alquiler alquiler) {
        return alquilerRepository.save(alquiler);
    }

    @Override
    public Alquiler getAlquilerById(Long id) {

        Optional<Alquiler> alquilerOptional = alquilerRepository.findById(id);
        return alquilerOptional.get();
    }

    @Override
    public List<Alquiler> getAllAlquilers() {
        return alquilerRepository.findAll();
    }

    @Override
    public void deleteAlquiler(Long id) {
        alquilerRepository.deleteById(id);
    }

    @Override
    public Alquiler updateAlquiler(Long id, Alquiler alquiler) {
        if (alquilerRepository.existsById(id)) {
            alquiler.setId(id);
            return alquilerRepository.save(alquiler);
        } else {
            throw new RuntimeException("Alquiler not found");
        }
    }

    @Override
    public Alquiler iniciarAlquiler(String idCliente, Long estacionRetiroId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<EstacionDTO> estaciones = traerEstaciones();

        // Verificar si la estación con el estacionRetiroId existe
        boolean estacionExiste = estaciones.stream()
                .anyMatch(estacion -> estacion.getId().equals(estacionRetiroId));

        if (estacionExiste) {
            // La estación existe, proceder con la creación del alquiler
            Alquiler alquiler = new Alquiler();
            alquiler.setIdCliente(idCliente);
            alquiler.setEstacionRetiro(estacionRetiroId);
            alquiler.setFechaHoraRetiro(localDateTime);
            alquiler.setEstado(1); //Iniciado
            // Guardar el registro de alquiler en la base de datos
            return alquilerRepository.save(alquiler);
        } else {
            // La estación no existe, puedes manejar este caso según tus necesidades
            throw new RuntimeException("La estación con ID " + estacionRetiroId + " no existe.");
        }
    }


    @Override
    public Alquiler finalizarAlquiler(Long idAlquiler, Long estacionDevolucionId, String moneda) {
        Tarifa tarifa = tarifaService.getTarifa();
        Alquiler alquiler = alquilerRepository.findById(idAlquiler).get();
        alquiler.setEstacionDevolucion(estacionDevolucionId);
        alquiler.setFechaHoraDevolucion(LocalDateTime.now());
        alquiler.setEstado(2);
        try {
            if (moneda != null) {
                System.out.println("Entro por el si");
                alquiler.setMonto(monedaService.convertirMoneda(moneda, tarifaService.calcularTarifa(alquiler, tarifa, estacionDevolucionId)));

            }else {
                alquiler.setMonto(tarifaService.calcularTarifa(alquiler, tarifa, estacionDevolucionId));
                System.out.println("Entro por el else");
            }
        }catch (RuntimeException e){
            throw new RuntimeException();
        }

        alquiler.setTarifa(tarifa);
        return alquilerRepository.save(alquiler);

    }

    @Override
    public List<Alquiler> getAllAlquilersWithFilter(String clienteId) {
        List<Alquiler> alquileres = getAllAlquilers().stream()
                .filter(alquiler -> alquiler.getIdCliente().equals(clienteId))
                .toList();
        if(alquileres.isEmpty()){
            throw new RuntimeException();
        } else {
            return alquileres;
        }
    }

    private static final String URI_ESTACIONES = "http://localhost:8082/api/estacion";

    @Override
    public List<EstacionDTO> traerEstaciones() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<List<EstacionDTO>> response = restTemplate.exchange(
                    URI_ESTACIONES,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<EstacionDTO>>() {}
            );

            List<EstacionDTO> estaciones = response.getBody();
            System.out.println("Estaciones obtenidas correctamente: " + estaciones);

            return estaciones;
        } catch (HttpClientErrorException.NotFound notFound) {
            System.out.println("Estación no encontrada (404)");
            return Collections.emptyList(); // o maneja el caso según tus necesidades
        } catch (Exception ex) {
            System.out.println("Error en la petición: " + ex.getMessage());
            return Collections.emptyList(); // o maneja el caso según tus necesidades
        }
    }


}
