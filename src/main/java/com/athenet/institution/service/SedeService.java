package com.athenet.institution.service;

import com.athenet.institution.dto.request.SedeRequest;
import com.athenet.institution.dto.response.SedeResponse;
import com.athenet.institution.exception.ResourceNotFoundException;
import com.athenet.institution.mapper.SedeMapper;
import com.athenet.institution.model.Institucion;
import com.athenet.institution.model.Sede;
import com.athenet.institution.repository.InstitucionRepository;
import com.athenet.institution.repository.SedeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SedeService {

    private final SedeRepository sedeRepository;
    private final InstitucionRepository institucionRepository;
    private final SedeMapper sedeMapper;

    /**
     * Crea una nueva sede.
     * Busca primero la institución indicada en el request (falla si no existe),
     * y recién con esa institución arma la entidad Sede completa.
     */
    @Transactional
    public SedeResponse crear(SedeRequest request) {
        Institucion institucion = buscarInstitucion(request.institucionId());
        Sede sede = sedeMapper.toEntity(request, institucion);
        return sedeMapper.toResponse(sedeRepository.save(sede));
    }

    /**
     * Busca una sede por su id.
     */
    public SedeResponse obtenerPorId(Long id) {
        return sedeMapper.toResponse(buscarEntidadPorId(id));
    }

    /**
     * Devuelve todas las sedes registradas.
     */
    public List<SedeResponse> listarTodas() {
        return sedeRepository.findAll().stream()
                .map(sedeMapper::toResponse)
                .toList();
    }

    /**
     * Devuelve las sedes que pertenecen a una institución específica.
     */
    public List<SedeResponse> listarPorInstitucion(Long institucionId) {
        return sedeRepository.findByInstitucionId(institucionId).stream()
                .map(sedeMapper::toResponse)
                .toList();
    }

    /**
     * Actualiza una sede existente, incluyendo la posibilidad
     * de reasignarla a otra institución.
     */
    @Transactional
    public SedeResponse actualizar(Long id, SedeRequest request) {
        Sede sede = buscarEntidadPorId(id);
        Institucion institucion = buscarInstitucion(request.institucionId());
        sedeMapper.updateEntity(sede, request, institucion);
        return sedeMapper.toResponse(sedeRepository.save(sede));
    }

    /**
     * Elimina una sede por su id.
     */
    @Transactional
    public void eliminar(Long id) {
        Sede sede = buscarEntidadPorId(id);
        sedeRepository.delete(sede);
    }

    private Sede buscarEntidadPorId(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede con id " + id + " no fue encontrada"));
    }

    /**
     * Método de apoyo: busca la Institucion relacionada a una Sede
     */
    private Institucion buscarInstitucion(Long institucionId) {
        return institucionRepository.findById(institucionId)
                .orElseThrow(() -> new ResourceNotFoundException("Institución con id " + institucionId + " no fue encontrada"));
    }
}