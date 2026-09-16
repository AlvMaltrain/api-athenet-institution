package com.athenet.institution.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athenet.institution.dto.request.DeporteRequest;
import com.athenet.institution.dto.response.DeporteResponse;
import com.athenet.institution.exception.ConflictException;
import com.athenet.institution.exception.ResourceNotFoundException;
import com.athenet.institution.mapper.DeporteMapper;
import com.athenet.institution.model.Deporte;
import com.athenet.institution.repository.DeporteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeporteService {

    private final DeporteRepository deporteRepository;
    private final DeporteMapper deporteMapper;

    /**
     * Crea un deporte a partir de los datos recibidos (request)
     * Convierte el Request a entidad y la guarda en la bd
     * devuelve el Response con el id ya generado
     */
    @Transactional
    public DeporteResponse crear(DeporteRequest request) {

        if (deporteRepository.existsByNombreIgnoreCase(request.nombre())){
            throw new ConflictException("Ya existe un deporte con el nombre '" + request.nombre() + "'");
        }
        Deporte deporte = deporteMapper.toEntity(request);
        return deporteMapper.toResponse(deporteRepository.save(deporte));
    }

    /**
     * Busca un deporte por su id
     * Lanza excepción si no existe ningún registro con ese id
     */
    public DeporteResponse obtenerPorId(Long id) {
        return deporteMapper.toResponse(buscarEntidadPorId(id));
    }

    /**
     * Devuelve todos los deportes registrados.
     * Si no hay, devuelve una lista vacía.
     */
    public List<DeporteResponse> listarTodos() {
        return deporteRepository.findAll().stream()
                .map(deporteMapper::toResponse)
                .toList();
    }

    /**
     * Actualiza un deporte existente con los nuevos datos recibidos.
     * Primero busca el registro actual (falla si no existe), luego
     * sobrescribe sus campos y guarda los cambios.
     */
    @Transactional
    public DeporteResponse actualizar(Long id, DeporteRequest request) {
        Deporte deporte = buscarEntidadPorId(id);

        deporteRepository.findByNombreIgnoreCase(request.nombre())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new ConflictException("Ya existe un deporte con el nombre '" + request.nombre() + "'");
                });

        deporteMapper.updateEntity(deporte, request);
        return deporteMapper.toResponse(deporteRepository.save(deporte));
    }

    /**
     * Elimina un deporte por su id.
     * Si tiene equipos (u otros registros) que dependen de él, la base
     * de datos rechaza el DELETE por violación de llave foránea; en ese
     * caso lo traducimos a un 409 explícito en vez del genérico de la red
     * de seguridad de GlobalExceptionHandler.
     */
    @Transactional
    public void eliminar(Long id) {
        Deporte deporte = buscarEntidadPorId(id);
        try {
            deporteRepository.delete(deporte);
            deporteRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    "No se puede eliminar el deporte '" + deporte.getNombre()
                            + "' porque tiene equipos (u otros registros) asociados. Elimina o reasigna esos registros primero."
            );
        }
    }

    /**
     * Busca la entidad Deporte por id,
     * o lanza una excepción. Evita repetir esta
     * misma búsqueda en obtenerPorId, actualizar y eliminar.
     */
    private Deporte buscarEntidadPorId(Long id) {
        return deporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deporte con id " + id + " no fue encontrado"));
    }
}