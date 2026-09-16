package com.athenet.institution.service;

import com.athenet.institution.dto.request.InstitucionRequest;
import com.athenet.institution.dto.response.InstitucionResponse;
import com.athenet.institution.mapper.InstitucionMapper;
import com.athenet.institution.model.Institucion;
import com.athenet.institution.repository.InstitucionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
     * Si tiene sedes (u otros registros) que dependen de ella, la base de
     * datos rechaza el DELETE por violación de llave foránea; en ese caso
     * lo traducimos a un 409 explícito en vez del genérico de la red de
     * seguridad de GlobalExceptionHandler.
     */
    @Transactional
    public void eliminar(Long id) {
        Institucion institucion = buscarEntidadPorId(id);
        try {
            institucionRepository.delete(institucion);
            institucionRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    "No se puede eliminar la institución '" + institucion.getNombre()
                            + "' porque tiene sedes (u otros registros) asociados. Elimina o reasigna esos registros primero."
            );
        }
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
