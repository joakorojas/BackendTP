package com.grupo10.tpi.apialquileres.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonedaRequest {

    String moneda_destino;
    Double importe;
}
