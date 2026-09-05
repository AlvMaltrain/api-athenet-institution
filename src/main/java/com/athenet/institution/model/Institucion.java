package com.athenet.institution.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;


@Entity
@Table(name = "instituciones")
@Schema(description = "Entidad que representa una institución educativa ")
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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

    @Schema (description = "Ciudad de la institución")
    private String ciudad;

    @Schema (description = "Región de la institución")
    private String region;

    @Schema (description = "Actividad de la institución")
    private boolean activo = true; 

}