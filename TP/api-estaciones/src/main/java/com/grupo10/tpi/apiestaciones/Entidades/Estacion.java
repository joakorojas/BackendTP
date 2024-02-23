package com.grupo10.tpi.apiestaciones.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Table(name = "ESTACIONES")
@NoArgsConstructor
@AllArgsConstructor
public class Estacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "FECHA_HORA_CREACION")
    private String fechaHoraCreacion;

    public LocalDateTime getCreacionDate() {
        if (fechaHoraCreacion == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(fechaHoraCreacion, formatter);
    }

    public void setCreacionDate(LocalDateTime creacionDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        fechaHoraCreacion = creacionDate.format(formatter);
    }

    @Column(name = "LATITUD")
    private Double latitud;

    @Column(name = "LONGITUD")
    private Double longitud;

    // Constructor, getters, and sette

}
