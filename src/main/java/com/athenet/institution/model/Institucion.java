package com.athenet.institution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "institucion")
@Schema(description = "Entidad que representa una institución educativa ")
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la institución")
    private Long id;

    @Schema(description = "Nombre de la institución")
    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Schema(description = "Sigla de la institución")
    private String sigla;

    @Schema (description = "URL de la imagen o logo de la institución")
    private String imagenUrl;

    @Schema (description = "Actividad de la institución")
    private boolean activo = true; 

    public Long getId() {
    return id;
}

public String getNombre() {
    return nombre;
}
}

