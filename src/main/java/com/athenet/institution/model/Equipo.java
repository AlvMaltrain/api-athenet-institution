package com.athenet.institution.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import io.swagger.v3.oas.annotations.media.Schema;

import com.athenet.institution.model.enums.Categoria;



@Entity
@Table(name = "equipo")
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Schema(description = "Entidad que representa un equipo deportivo de una institución")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del equipo")
    private Long id;

    @Column(nullable = false, length = 150)
    @Schema(description = "Nombre del equipo")
    private String nombre;

    @Schema(description = "Categoría del equipo", example = "Masculino")
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Schema(description = "Coach o entrenador del equipo")
    private String entrenador;

    @Schema(description = "Logo del equipo")
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Schema(description = "Deporte al que pertenece el equipo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deporte_id", nullable = false)
    private Deporte deporte;
}
