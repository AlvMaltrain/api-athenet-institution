package com.athenet.institution.dto.response;

public record InstitucionResponse(
    Long id,
    String nombre,
    String sigla,
    String ciudad,
    String region,
    boolean activo
){
    
}

