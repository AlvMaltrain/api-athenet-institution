package com.athenet.institution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athenet.institution.model.Deporte;

public interface DeporteRepository extends JpaRepository<Deporte,Long> {

    Optional<Deporte> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}