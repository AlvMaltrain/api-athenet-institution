package com.athenet.institution.dto.response;

import com.athenet.institution.model.enums.Categoria;

public record EquipoResponse(
    Long id,
    String nombre,
    Categoria categoria,
    String entrenador, 
    String logoUrl,
    Long sedeId,
    String sedeNombre,
    Long deporteId,
    String deporteNombre,
    boolean activo
) {
    
}
