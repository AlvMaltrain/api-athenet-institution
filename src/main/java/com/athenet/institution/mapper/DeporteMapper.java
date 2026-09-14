package com.athenet.institution.mapper;

import org.springframework.stereotype.Component;

import com.athenet.institution.dto.request.DeporteRequest;
import com.athenet.institution.dto.response.DeporteResponse;
import com.athenet.institution.model.Deporte;

@Component
public class DeporteMapper {
    public Deporte toEntity(DeporteRequest request) {
        Deporte deporte = new Deporte();
        deporte.setNombre(request.nombre());
        deporte.setDescripcion(request.descripcion());
        return deporte;
    }

    public void updateEntity(Deporte deporte, DeporteRequest request) {
        deporte.setNombre(request.nombre());
        deporte.setDescripcion(request.descripcion());
    }

    public DeporteResponse toResponse(Deporte deporte) {
        return new DeporteResponse(
            deporte.getId(),
            deporte.getNombre(),
            deporte.getDescripcion()
        );
    }
}