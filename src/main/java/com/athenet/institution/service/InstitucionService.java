package com.athenet.institution.service;

import com.athenet.institution.dto.request.InstitucionRequest;
import com.athenet.institution.dto.response.InstitucionResponse;
import com.athenet.institution.mapper.InstitucionMapper;
import com.athenet.institution.model.Institucion;
import com.athenet.institution.repository.InstitucionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.athenet.institution.exception.ConflictException;
import com.athenet.institution.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstitucionService {

    private final InstitucionRepository institucionRepository;
    private final InstitucionMapper institucionMapper;

    /**
     * Crea una institución.
     * Antes de guardar, valida que no exista otra institución con el mismo nombre.
     */
    @Transactional
    public InstitucionResponse crear(InstitucionRequest request) {
        if (institucionRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new ConflictException("Ya existe una institución con el nombre '" + request.nombre() + "'");
        }
        Institucion institucion = institucionMapper.toEntity(request);
        return institucionMapper.toResponse(institucionRepository.save(institucion));
    }

    /**
     * Busca una institución por su id.
     */
    public InstitucionResponse obtenerPorId(Long id) {
        return institucionMapper.toResponse(buscarEntidadPorId(id));
    }

    /**
     * Devuelve todas las instituciones registradas.
     */
    public List<InstitucionResponse> listarTodas() {
        return institucionRepository.findAll().stream()
                .map(institucionMapper::toResponse)
                .toList();
    }

    /**
     * Actualiza una institución existente.
     */
    @Transactional
    public InstitucionResponse actualizar(Long id, InstitucionRequest request) {
        Institucion institucion = buscarEntidadPorId(id);

        institucionRepository.findByNombreIgnoreCase(request.nombre())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new ConflictException("Ya existe una institución con el nombre '" + request.nombre() + "'");
                });

        institucionMapper.updateEntity(institucion, request);
        return institucionMapper.toResponse(institucionRepository.save(institucion));
    }

    /**
     * Elimina una institución por su id.
     */
    @Transactional
    public void eliminar(Long id) {
        Institucion institucion = buscarEntidadPorId(id);
        institucionRepository.delete(institucion);
    }

    /**
     * Método interno de apoyo: busca la entidad Institucion por id,
     * o lanza una excepción si no existe.
     */
    private Institucion buscarEntidadPorId(Long id) {
        return institucionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Institución con id " + id + " no fue encontrada"));
    }
}