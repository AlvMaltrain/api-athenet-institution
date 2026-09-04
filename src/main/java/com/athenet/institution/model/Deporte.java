package com.athenet.institution.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "deporte")
@Schema(description = "Entidad que representa un deporte practicado en una institución")
public class Deporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del deporte")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Nombre del deporte")
    private String nombre;
   
}
