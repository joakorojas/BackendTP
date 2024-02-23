package com.grupo10.tpi.apialquileres.controllers;

import com.grupo10.tpi.apialquileres.entities.Alquiler;
import com.grupo10.tpi.apialquileres.services.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import io.swagger.annotations.*;

@RestController
@Api(tags = "Alquiler", description = "Operaciones con alquiler")
@RequestMapping("/api/alquiler")
public class AlquilerController {
    private final AlquilerService alquilerService;


    @Autowired
    public AlquilerController(AlquilerService alquilerService) {

        this.alquilerService = alquilerService;
    }


        @ApiOperation("Obtener una tarifa por su ID")
        @GetMapping("/{id}")
        public ResponseEntity<Alquiler> getAlquilerById(@PathVariable("id") Long id) {
            Alquiler alquiler = alquilerService.getAlquilerById(id);
            return ResponseEntity.ok(alquiler);
        }

        @ApiOperation("Crear una nueva tarifa")
        @PostMapping
        public ResponseEntity<Alquiler> createAlquiler(@RequestBody Alquiler alquiler) {
            Alquiler createdAlquiler = alquilerService.createAlquiler(alquiler);
            return ResponseEntity.ok(createdAlquiler);
        }

        @ApiOperation("Obtener todas las tarifas")
        @GetMapping
        public List<Alquiler> getAllAlquilers() {
            return alquilerService.getAllAlquilers();
        }

        @ApiOperation("Eliminar una tarifa por su ID")
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteAlquiler(@PathVariable("id") Long id) {
            alquilerService.deleteAlquiler(id);
            return ResponseEntity.ok("Alquiler deleted");
        }

        @ApiOperation("Actualizar una tarifa por su ID")
        @PutMapping("/{id}")
        public ResponseEntity<?> updateAlquiler(@PathVariable("id") Long id, @RequestBody Alquiler alquiler) {
            try {
                Alquiler updatedAlquiler = alquilerService.updateAlquiler(id, alquiler);
                return ResponseEntity.ok(updatedAlquiler);
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        }

            @ApiOperation("Iniciar un alquiler")
            @ApiResponses({
                    @ApiResponse(code = 200, message = "Alquiler iniciado satisfactoriamente", response = Alquiler.class),
                    @ApiResponse(code = 400, message = "Error al iniciar el alquiler")
            })
            @PostMapping("/iniciar")
            public ResponseEntity<Alquiler> iniciarAlquiler (
                    @ApiParam(value = "ID del cliente", required = true) @RequestParam("idCliente") String idCliente,
                    @ApiParam(value = "ID de la estación de retiro", required = true) @RequestParam("estacionRetiroId") Long
            estacionRetiroId){
                try {
                    Alquiler alquiler = alquilerService.iniciarAlquiler(idCliente, estacionRetiroId);
                    return ResponseEntity.ok(alquiler);
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().build();
                }
            }
            @PutMapping("/finalizar")
            public ResponseEntity<Object> finalizarAlquiler (@RequestParam Long idAlquiler, @RequestParam Long
            estacionDevolucionId, @RequestParam(required = false) String moneda){
                try {
                    Alquiler alquiler = alquilerService.finalizarAlquiler(idAlquiler, estacionDevolucionId, moneda);
                    return ResponseEntity.ok(alquiler);
                } catch (RuntimeException e) {
                    return ResponseEntity.notFound().build();
                }
            }

            @GetMapping("/{clienteId}")
            public ResponseEntity<List<Alquiler>> getAllAlquilersWithFilter (@PathVariable String clienteId){
                try {
                    List<Alquiler> alquilers = alquilerService.getAllAlquilersWithFilter(clienteId);
                    return ResponseEntity.ok(alquilers);
                } catch (RuntimeException e) {
                    return ResponseEntity.notFound().build();
                }
            }


}
