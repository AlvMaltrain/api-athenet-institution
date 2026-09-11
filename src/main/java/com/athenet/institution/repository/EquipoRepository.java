package com.athenet.institution.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athenet.institution.model.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo,Long> {
    
    List<Equipo> findBySedeId(Long sedeId); //Obtiene todos los equipos de una sede
    List<Equipo> findByDeporteId(Long deporteId); //Obtiene todos los equipos de un deporte 

    List<Equipo> findByDeporteIdAndSede_Institucion_IdNot(Long deporteId, Long institucionId); //Obtiene todos los equipos de un deporte que no pertenecen a una institución específica
}
