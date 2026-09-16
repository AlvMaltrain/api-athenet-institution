package com.athenet.institution.mapper;

import org.springframework.stereotype.Component;

import com.athenet.institution.dto.request.InstitucionRequest;
import com.athenet.institution.dto.response.InstitucionResponse;
import com.athenet.institution.model.Institucion;

@Component
public class InstitucionMapper {

    public Institucion toEntity(InstitucionRequest request) {
        Institucion institucion = new Institucion();
        institucion.setNombre(request.nombre());
        institucion.setSigla(request.sigla());
        institucion.setImagenUrl(request.imagenUrl());
        institucion.setActivo(request.activo());
        return institucion;
    }

    public void updateEntity(Institucion institucion, InstitucionRequest request) {
        institucion.setNombre(request.nombre());
        institucion.setSigla(request.sigla());
        institucion.setImagenUrl(request.imagenUrl());
        institucion.setActivo(request.activo());
    }

    public InstitucionResponse toResponse(Institucion institucion) {
        return new InstitucionResponse(
                institucion.getId(),
                institucion.getNombre(),
                institucion.getSigla(),
                institucion.getImagenUrl(),
                institucion.isActivo()
        );
    }
}