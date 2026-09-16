package com.athenet.institution.dto.response;

public record SedeResponse(
    Long id,
    String nombre,
    String ciudad,
    String direccion,
    Long institucionId,
    String institucionNombre
) {
    
}
