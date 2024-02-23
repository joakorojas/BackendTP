package com.grupo10.tpi.apialquileres.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ALQUILERES")
@AllArgsConstructor
@NoArgsConstructor
public class Alquiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_CLIENTE", length = 50)
    private String idCliente;

    @Column(name = "ESTADO")
    private Integer estado;


    @JoinColumn(name = "ESTACION_RETIRO")
    private Long estacionRetiro;


    @JoinColumn(name = "ESTACION_DEVOLUCION")
    private Long estacionDevolucion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_HORA_RETIRO")
    private LocalDateTime fechaHoraRetiro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_HORA_DEVOLUCION")
    private LocalDateTime fechaHoraDevolucion;

    @Column(name = "MONTO")
    private Double monto;

    @ManyToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifa tarifa;


}
