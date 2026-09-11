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

import com.athenet.institution.dto.request.InstitucionRequest;
import com.athenet.institution.dto.response.InstitucionResponse;
import com.athenet.institution.service.InstitucionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/instituciones")
@RequiredArgsConstructor
public class InstitucionController {

    private final InstitucionService institucionService;

    @PostMapping
    public ResponseEntity<InstitucionResponse> crear(@Valid @RequestBody InstitucionRequest request) {
        InstitucionResponse creada = institucionService.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/instituciones/" + creada.id())).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitucionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(institucionService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<InstitucionResponse>> listarTodas() {
        return ResponseEntity.ok(institucionService.listarTodas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitucionResponse> actualizar(@PathVariable Long id,
        @Valid @RequestBody InstitucionRequest request) {
        return ResponseEntity.ok(institucionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        institucionService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}