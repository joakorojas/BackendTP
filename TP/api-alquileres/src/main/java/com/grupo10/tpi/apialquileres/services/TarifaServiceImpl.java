package com.grupo10.tpi.apialquileres.services;

import com.grupo10.tpi.apialquileres.dtos.EstacionDTO;
import com.grupo10.tpi.apialquileres.entities.Alquiler;
import com.grupo10.tpi.apialquileres.entities.Tarifa;
import com.grupo10.tpi.apialquileres.repositories.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TarifaServiceImpl implements TarifaService {
    //inyeccion de repositorios
    private final TarifaRepository tarifaRepository;


    //inyeccion de instancias de los repositorios
    @Autowired
    public TarifaServiceImpl(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    @Override
    public Tarifa createTarifa(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    @Override
    public Optional<Tarifa> getTarifaById(Long id) {
        return tarifaRepository.findById(id);
    }

    @Override
    public List<Tarifa> getAllTarifas() {
        return tarifaRepository.findAll();
    }

    @Override
    public void deleteTarifa(Long id) {
        tarifaRepository.deleteById(id);
    }

    @Override
    public Tarifa updateTarifa(Long id, Tarifa tarifa) {
        if (tarifaRepository.existsById(id)) {
            tarifa.setId(id);
            return tarifaRepository.save(tarifa);
        } else {
            throw new RuntimeException("Tarifa not found");
        }
    }

    public Tarifa getTarifa(){
        LocalDateTime fecha_actual = LocalDateTime.now();
        //Obtener tarifa segun si es normal o con descuento
        Optional<Tarifa> optionalTarifa =tarifaRepository.getTarifaByDiaMesAndMesAndAnio(fecha_actual.getDayOfMonth()
                ,fecha_actual.getMonth().getValue(),fecha_actual.getYear());
        if(optionalTarifa.isPresent()){
            return optionalTarifa.get();
        }else {
            return tarifaRepository.getTarifaByDiaSemana(fecha_actual.getDayOfWeek().getValue());

        }
    }

    public Optional<EstacionDTO> buscarEstacion(Long estacionId){
        List<EstacionDTO> estaciones = traerEstaciones();
        Optional<EstacionDTO> estacionOptional = estaciones.stream()
                .filter(estacion -> estacion.getId().equals(estacionId))
                .findFirst();
        if (estacionOptional.isPresent()) {
            return estacionOptional;
        }else{
            throw new RuntimeException();
        }
    }

    public double calcularTarifa(Alquiler alquiler, Tarifa tarifa, Long estacionDevolucionId) {
        LocalDateTime fecha_actual = LocalDateTime.now();
        Duration duracion = Duration.between((Temporal) alquiler.getFechaHoraRetiro(), fecha_actual);

        //cantidad de horas y minutos que duro el alquiler
        Long duracion_horas = duracion.toHours();
        Long duracion_minutos = duracion.toMinutes()- (duracion_horas*60);

        //Tarifas
        double tarifa_total = 0; //inicializa en 0

        //monto fijo
        tarifa_total += tarifa.getMontoFijoAlquiler();
        System.out.println(tarifa_total);

        //monto por hora y minutos

        //a partir del min 31 --> HORA COMPLETA

        if(duracion_minutos >= 31 ){
            tarifa_total += tarifa.getMontoHora() * (duracion_horas + 1);
            System.out.println(tarifa_total);

        }else {
            tarifa_total += tarifa.getMontoHora() * duracion_horas;
            tarifa_total += tarifa.getMontoMinutoFraccion() * duracion_minutos;
            System.out.println(tarifa_total);
        }



        //monto por distancia
        try {
            Optional<EstacionDTO> estacion_retiro1 = buscarEstacion(alquiler.getEstacionRetiro());
            Optional<EstacionDTO> estacion_devolucion1 = buscarEstacion(estacionDevolucionId);
            //por aca km que separa las estaciones se cobra monto adicional

            if (estacion_retiro1.isPresent() && estacion_devolucion1.isPresent()) {
                EstacionDTO estacion_retiro = estacion_retiro1.get();
                EstacionDTO estacion_devolucion = estacion_devolucion1.get();

                tarifa_total += calcularDistancia(
                        estacion_retiro.getLatitud(),
                        estacion_retiro.getLongitud(),
                        estacion_devolucion.getLatitud(),
                        estacion_devolucion.getLongitud())
                        * tarifa.getMontoKm();
                System.out.println(tarifa_total);

                return tarifa_total;

            } else {
                // Manejar el caso en el que alguno de los Optionals está vacío
                throw new RuntimeException("No se encontró alguna de las estaciones.");
            }
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
    }


    private static final String URI_ESTACIONES = "http://localhost:8082/api/estacion/calcularDistancia";

    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        RestTemplate restTemplate = new RestTemplate();

        // Crear la URL con los parámetros
        String url = String.format("%s?lat1=%s&lon1=%s&lat2=%s&lon2=%s", URI_ESTACIONES, lat1, lon1, lat2, lon2);

        try {
            // Hacer la solicitud HTTP y obtener la respuesta como un Double
            Double response = restTemplate.getForObject(url, Double.class);

            if (response != null) {
                return response; // Devolver la distancia calculada
            } else {
                // Manejar el caso en el que la respuesta es nula
                throw new RuntimeException("La respuesta de la API fue nula.");
            }
        } catch (Exception e) {
            // Manejar excepciones, por ejemplo, imprimir un mensaje de error
            e.printStackTrace();
            throw new RuntimeException("Error al hacer la solicitud a la API.", e);
        }
    }

    private static final String URI_ESTACIONES1 = "http://localhost:8082/api/estacion";
    public List<EstacionDTO> traerEstaciones() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<List<EstacionDTO>> response = restTemplate.exchange(
                    URI_ESTACIONES1,
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

