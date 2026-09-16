package com.athenet.institution.mapper;

import org.springframework.stereotype.Component;

import com.athenet.institution.dto.request.EquipoRequest;
import com.athenet.institution.dto.response.EquipoResponse;
import com.athenet.institution.model.Deporte;
import com.athenet.institution.model.Equipo;
import com.athenet.institution.model.Sede;

@Component
public class EquipoMapper {

    public Equipo toEntity(EquipoRequest request, Sede sede, Deporte deporte) {
        Equipo equipo = new Equipo();
        equipo.setNombre(request.nombre());
        equipo.setCategoria(request.categoria());
        equipo.setEntrenador(request.entrenador());
        equipo.setLogoUrl(request.logoUrl());
        equipo.setSede(sede);
        equipo.setDeporte(deporte);
        return equipo;
    }

    public void updateEntity(Equipo equipo, EquipoRequest request, Sede sede, Deporte deporte) {
        equipo.setNombre(request.nombre());
        equipo.setCategoria(request.categoria());
        equipo.setEntrenador(request.entrenador());
        equipo.setLogoUrl(request.logoUrl());
        equipo.setSede(sede);
        equipo.setDeporte(deporte);
    }

    public EquipoResponse toResponse(Equipo equipo) {
        return new EquipoResponse(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getCategoria(),
                equipo.getEntrenador(),
                equipo.getLogoUrl(),
                equipo.getSede().getId(),
                equipo.getSede().getNombre(),
                equipo.getDeporte().getId(),
                equipo.getDeporte().getNombre(),
                equipo.isActivo()
        );
    }
}