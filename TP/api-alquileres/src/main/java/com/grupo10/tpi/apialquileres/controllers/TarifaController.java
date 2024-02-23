package com.grupo10.tpi.apialquileres.controllers;

import com.grupo10.tpi.apialquileres.entities.Tarifa;
import com.grupo10.tpi.apialquileres.services.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Tarifa", description = "Operaciones con tarifas")
@RequestMapping("/api/tarifa") //http://localhost:8080/tarifa
public class TarifaController {
    private final TarifaService tarifaService;
    @Autowired
    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @ApiOperation("Obtener una tarifa por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTarifaById(@PathVariable("id") Long id) {
        Optional<Tarifa> tarifa = tarifaService.getTarifaById(id);
        return tarifa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation("Crear una nueva tarifa")
    @PostMapping
    public ResponseEntity<Tarifa> createTarifa(@RequestBody Tarifa tarifa) {
        Tarifa createdTarifa = tarifaService.createTarifa(tarifa);
        return ResponseEntity.ok(createdTarifa);
    }

    @ApiOperation("Obtener todas las tarifas")
    @GetMapping
    public List<Tarifa> getAllTarifas() {
        return tarifaService.getAllTarifas();
    }


    @ApiOperation("Eliminar una tarifa por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTarifa(@PathVariable("id") Long id) {
        tarifaService.deleteTarifa(id);
        return ResponseEntity.ok("Tarifa deleted");
    }

    @ApiOperation("Actualizar una tarifa por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTarifa(@PathVariable("id") Long id, @RequestBody Tarifa tarifa) {
        try {
            Tarifa updatedTarifa = tarifaService.updateTarifa(id, tarifa);
            return ResponseEntity.ok(updatedTarifa);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}

