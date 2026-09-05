package com.athenet.institution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "jugador")
@Schema(description = "Entidad que representa un jugador de un equipo deportivo")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del jugador")
    private Long id;

    @Column(nullable = false, length = 200)
    @Schema(description = "Nombre del jugador")
    private String nombre;

    @Column(nullable = false, length = 200)
    @Schema(description = "Apellido del jugador")
    private String apellido;

    @Schema(description = "Edad del jugador")
    private int edad;

    @Schema(description = "Foto del jugador")
    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Schema(description = "Equipo al que pertenece el jugador")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;
    
}
