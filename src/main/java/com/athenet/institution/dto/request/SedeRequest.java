package com.athenet.institution.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SedeRequest(
    
    @NotBlank (message = "El nombre de la sede es obligatorio")
    @Size (max = 100, message = "El nombre de la sede no puede superar los 100 caracteres")
    String nombre,

    @NotBlank (message = "El nombre de la ciudad es obligatorio")
    @Size (max = 100, message = "El nombre de la ciudad no puede superar los 100 caracteres")
    String ciudad,

    @NotBlank (message = "La dirección de la sede es obligatoria")
    @Size (max = 100, message = "La dirección de la sede no puede superar los 100 caracteres")
    String direccion,

    @NotNull (message = "El id de la institución es obligatorio")
    Long institucionId

){  
}
