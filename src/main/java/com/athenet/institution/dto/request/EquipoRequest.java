package com.athenet.institution.dto.request;

import com.athenet.institution.model.enums.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipoRequest(

    @NotBlank (message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    String nombre,

    @NotNull(message = "La categoría es obligatoria")
    Categoria categoria,

    @Size(max = 150, message = "El nombre del entrenador no puede superar los 150 caracteres")
    String entrenador,

    @Size(max = 500, message = "La URL no puede superar los 500 caracteres")
    String logoUrl,

    @NotNull(message = "El id de la sede es obligatorio")
    Long sedeId,

    @NotNull(message = "El id del deporte es obligatorio")
    Long deporteId
) {
    
}
