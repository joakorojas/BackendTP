package com.grupo10.tpi.apiestaciones.Controllers;
import io.swagger.annotations.*;
import com.grupo10.tpi.apiestaciones.dto.EstacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import com.grupo10.tpi.apiestaciones.Entidades.Estacion;
import com.grupo10.tpi.apiestaciones.Services.EstacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "Estacion", description = "Operaciones con estacion")
@RestController
@RequestMapping("/api/estacion") //http://localhost:8080/estacion
public class EstacionController {
    private final EstacionService estacionService;
    @Autowired
    public EstacionController(EstacionService estacionService) {
        this.estacionService = estacionService;
    }

    @ApiOperation("Obtener una tarifa por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EstacionDTO> getEstacionById(@PathVariable("id") Long id) {
        EstacionDTO estacion = estacionService.getEstacionById(id);
        if (estacion != null) {
            return ResponseEntity.ok(estacion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Crear una nueva tarifa")
    @PostMapping
    public ResponseEntity<EstacionDTO> createEstacion(@RequestBody EstacionDTO estacion) {
        EstacionDTO createdEstacion = estacionService.createEstacion(estacion);
        return ResponseEntity.ok(createdEstacion);
    }

    @ApiOperation("Obtener todas las tarifas")
    @GetMapping
    public List<EstacionDTO> getAllEstacions() {
        return estacionService.getAllEstaciones();
    }


    @ApiOperation("Eliminar una tarifa por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEstacion(@PathVariable("id") Long id) {
        estacionService.deleteEstacion(id);
        return ResponseEntity.ok("Estacion deleted");
    }

    @ApiOperation("Actualizar una tarifa por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<EstacionDTO> updateEstacion(@PathVariable("id") Long id, @RequestBody EstacionDTO estacionDTO) {
        try {
            EstacionDTO updatedEstacion = estacionService.updateEstacion(id, estacionDTO);
            return ResponseEntity.ok(updatedEstacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @ApiOperation(value = "Obtener la estación más cercana")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Se devuelve la estación más cercana"),
            @ApiResponse(code = 404, message = "No se encontró ninguna estación cercana")
    })
    @GetMapping("/cercana") //http://localhost:8080/estacion/cercana?latitud=algo&longitud=algo
    public ResponseEntity<EstacionDTO> getEstacionMasCercana(
            @ApiParam(value = "Latitud de la ubicación", required = true)
            @RequestParam("latitud") double latitud,
            @ApiParam(value = "Longitud de la ubicación", required = true)
            @RequestParam("longitud") double longitud
    ) {
        Optional<EstacionDTO> estacionCercana = estacionService.getEstacionCercana(latitud, longitud);
        return estacionCercana
                .map(estacionDto -> ResponseEntity.ok(estacionDto))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/calcularDistancia")
    public double getCalcularDistancia(@RequestParam double lat1, @RequestParam double lon1, @RequestParam double lat2, @RequestParam double lon2){
        return estacionService.calcularDistancia(lat1, lon1, lat2, lon2);
    }
}

