package com.grupo10.tpi.apiestaciones.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class EstacionDTO {
    private Long id;
    private String nombre;
    private Double latitud;
    private Double longitud;
}
