package com.athenet.institution.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record InstitucionRequest(

    @NotBlank(message = "El nombre de la institución es obligatorio")
    @Size (max = 100, message = "El nombre de la institución no puede tener mas de 100 caracteres")
    String nombre,

    @NotBlank(message = "La sigla de la institución es obligatoria")
    @Size (max = 10, message = "La sigla no puede superar los 10 caracteres")
    String sigla,

    @Size(max = 255, message = "La URL de la imagen no puede superar los 255 caracteres")
    String imagenUrl
) {

}

