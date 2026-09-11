package com.athenet.institution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athenet.institution.model.Jugador;

public interface JugadorRepository extends JpaRepository<Jugador,Long> {
    
    List<Jugador> findByEquipoId(Long equipoId); //Obtiene todos los jugadores de un equipo
}
