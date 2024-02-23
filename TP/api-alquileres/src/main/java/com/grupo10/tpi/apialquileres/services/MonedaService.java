package com.grupo10.tpi.apialquileres.services;

import com.grupo10.tpi.apialquileres.entities.MonedaRequest;
import com.grupo10.tpi.apialquileres.entities.MonedaResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class MonedaService {


    public double convertirMoneda(String monedaDestino, double monto) {
        RestTemplate restTemplate = new RestTemplate();
        MonedaRequest monedaRequest = new MonedaRequest(monedaDestino, monto);
        ResponseEntity<MonedaResponse> res = restTemplate.postForEntity(
                "http://34.82.105.125:8080/convertir",
                monedaRequest,
                MonedaResponse.class
        );
        if (res.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(res.getBody()).getImporte();
        }
        return 0;

    }
}
