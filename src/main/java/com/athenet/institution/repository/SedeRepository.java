package com.athenet.institution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athenet.institution.model.Sede;

public interface SedeRepository extends JpaRepository<Sede,Long> {

    List<Sede> findByInstitucionId(Long institucionId); //Obtiene todas las sedes de una institución
}
