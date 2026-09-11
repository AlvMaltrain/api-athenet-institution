package com.athenet.institution.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athenet.institution.dto.request.EquipoRequest;
import com.athenet.institution.dto.response.EquipoResponse;
import com.athenet.institution.service.EquipoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService equipoService;

    @PostMapping
    public ResponseEntity<EquipoResponse> crear(@Valid @RequestBody EquipoRequest request) {
        EquipoResponse creado = equipoService.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/equipos/" + creado.id())).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<EquipoResponse>> listarTodos(
            @RequestParam(required = false) Long sedeId) {
        if (sedeId != null) {
            return ResponseEntity.ok(equipoService.listarPorSede(sedeId));
        }
        return ResponseEntity.ok(equipoService.listarTodos());
    }

    @GetMapping("/rivales")
    public ResponseEntity<List<EquipoResponse>> listarRivales(
            @RequestParam Long deporteId,
            @RequestParam Long institucionId) {
        return ResponseEntity.ok(equipoService.listarRivales(deporteId, institucionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponse> actualizar(@PathVariable Long id,
        @Valid @RequestBody EquipoRequest request) {
        return ResponseEntity.ok(equipoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        equipoService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}