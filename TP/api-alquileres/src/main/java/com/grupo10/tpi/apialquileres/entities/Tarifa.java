package com.grupo10.tpi.apialquileres.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "TARIFAS")
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TIPO_TARIFA")
    private Integer tipoTarifa;

    @Column(name = "DEFINICION", length = 1)
    private String definicion;

    @Column(name = "DIA_SEMANA")
    private Integer diaSemana;

    @Column(name = "DIA_MES")
    private Integer diaMes;

    @Column(name = "MES")
    private Integer mes;

    @Column(name = "ANIO")
    private Integer anio;

    @Column(name = "MONTO_FIJO_ALQUILER")
    private Double montoFijoAlquiler;

    @Column(name = "MONTO_MINUTO_FRACCION")
    private Double montoMinutoFraccion;

    @Column(name = "MONTO_KM")
    private Double montoKm;

    @Column(name = "MONTO_HORA")
    private Double montoHora;

    // Constructor, getters, and setter
}
