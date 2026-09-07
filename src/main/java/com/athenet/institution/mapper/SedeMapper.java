package com.athenet.institution.mapper;

import com.athenet.institution.dto.request.SedeRequest;
import com.athenet.institution.dto.response.SedeResponse;
import com.athenet.institution.model.Institucion;
import com.athenet.institution.model.Sede;
import org.springframework.stereotype.Component;

@Component
public class SedeMapper {

    public Sede toEntity(SedeRequest request, Institucion institucion) {
        Sede sede = new Sede();
        sede.setNombre(request.nombre());
        sede.setCiudad(request.ciudad());
        sede.setDireccion(request.direccion());
        sede.setInstitucion(institucion);
        return sede;
    }

    public void updateEntity(Sede sede, SedeRequest request, Institucion institucion) {
        sede.setNombre(request.nombre());
        sede.setCiudad(request.ciudad());
        sede.setDireccion(request.direccion());
        sede.setInstitucion(institucion);
    }

    public SedeResponse toResponse(Sede sede) {
        return new SedeResponse(
                sede.getId(),
                sede.getNombre(),
                sede.getCiudad(),
                sede.getDireccion(),
                sede.getInstitucion().getId(),
                sede.getInstitucion().getNombre()
        );
    }
}