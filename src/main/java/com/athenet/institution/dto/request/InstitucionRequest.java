package com.athenet.institution.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record InstitucionRequest(

    @NotBlank(message = "El nombre de la institución es obligatorio")
    @Size (max = 100, message = "El nombre de la institución no puede tener mas de 100 caracteres")
    String nombre,

    @NotBlank(message = "El nombre de la institución es obligatorio")
    @Size (max = 10, message = "La sigla no puede superar los 10 caracteres")
    String sigla,

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Size (max = 100, message = "El nombre de la ciudad no puede superar los 100 caracteres")
    String ciudad,

    @NotBlank(message = "El nombre de la región es obligatorio")
    @Size (max = 100, message = "El nombre de la región no puede tener mas de 100 caracteres")
    String region

) {

}

