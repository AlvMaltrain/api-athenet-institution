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
        return deporte;
    }

    public void updateEntity(Deporte deporte, DeporteRequest request) {
        deporte.setNombre(request.nombre());
    }

    public DeporteResponse toResponse(Deporte deporte) {
        return new DeporteResponse(
            deporte.getId(),
            deporte.getNombre()
        );
    }
}