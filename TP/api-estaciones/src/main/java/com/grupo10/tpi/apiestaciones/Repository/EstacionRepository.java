package com.grupo10.tpi.apiestaciones.Repository;

import com.grupo10.tpi.apiestaciones.Entidades.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstacionRepository extends JpaRepository<Estacion, Long> {
}
