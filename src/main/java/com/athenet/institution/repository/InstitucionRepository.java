package com.athenet.institution.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athenet.institution.model.Institucion;


public interface InstitucionRepository extends JpaRepository<Institucion,Long>  {

Optional<Institucion> findByNombreIgnoreCase(String nombre);

boolean existsByNombreIgnoreCase(String nombre);
}
