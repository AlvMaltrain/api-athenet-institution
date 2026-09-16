package com.athenet.institution.model;

import java.util.List;

import com.athenet.institution.model.enums.Categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "equipo", uniqueConstraints = @UniqueConstraint(columnNames = {"sede_id", "nombre"}))
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

    @Schema (description = "Jugador que pertenece al equipo")
    @OneToMany(mappedBy = "equipo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // cascade Replica operaciones en Equipo y Jugador, orpahnRemoval elimina jugadores si se eliminan del equipo
    private List<Jugador> jugadores;

    @Schema(description = "Sede a la que pertenece el equipo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;

    @Schema(description = "Estado del equipo")
    @Column(nullable = false)
    private boolean activo = true;
}
