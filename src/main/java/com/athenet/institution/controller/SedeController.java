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

import com.athenet.institution.dto.request.SedeRequest;
import com.athenet.institution.dto.response.SedeResponse;
import com.athenet.institution.service.SedeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sedes")
@RequiredArgsConstructor
public class SedeController {

    private final SedeService sedeService;

    @PostMapping
    public ResponseEntity<SedeResponse> crear(@Valid @RequestBody SedeRequest request) {
        SedeResponse creada = sedeService.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/sedes/" + creada.id())).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sedeService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<SedeResponse>> listarTodas(
            @RequestParam(required = false) Long institucionId) {
        if (institucionId != null) {
            return ResponseEntity.ok(sedeService.listarPorInstitucion(institucionId));
        }
        return ResponseEntity.ok(sedeService.listarTodas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeResponse> actualizar(@PathVariable Long id,
        @Valid @RequestBody SedeRequest request) {
        return ResponseEntity.ok(sedeService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sedeService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}