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
import org.springframework.web.bind.annotation.RestController;

import com.athenet.institution.dto.request.DeporteRequest;
import com.athenet.institution.dto.response.DeporteResponse;
import com.athenet.institution.service.DeporteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deportes")
@RequiredArgsConstructor
public class DeporteController {

    private final DeporteService deporteService;

    @PostMapping
    public ResponseEntity<DeporteResponse> crear(@Valid @RequestBody DeporteRequest request) {
        DeporteResponse creado = deporteService.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/deportes/" + creado.id())).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeporteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(deporteService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<DeporteResponse>> listarTodos() {
        return ResponseEntity.ok(deporteService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeporteResponse> actualizar(@PathVariable Long id,
        @Valid @RequestBody DeporteRequest request) {
        return ResponseEntity.ok(deporteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        deporteService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}