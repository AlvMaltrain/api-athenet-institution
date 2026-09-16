package com.athenet.institution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "sede")
@Schema(description = "Entidad que representa una sede de una institución")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la sede")
    private Long id;

    @Column(nullable = false, length = 150)
    @Schema(description = "Nombre de la sede")
    private String nombre;

    @Schema(description = "Dirección de la sede")
    private String direccion;

    @Schema(description = "Ciudad de la sede")
    private String ciudad;

    @Schema(description = "Región de la sede")
    private String region;

    @Schema(description = "Institución a la que pertenece la sede")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "institucion_id", nullable = false)
    private Institucion institucion;
    
}
