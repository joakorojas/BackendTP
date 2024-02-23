package com.grupo10.tpi.apialquileres.dtos;

import lombok.Data;

@Data
public class EstacionDTO {
    private Long id;
    private String nombre;
    private Double latitud;
    private Double longitud;
}
