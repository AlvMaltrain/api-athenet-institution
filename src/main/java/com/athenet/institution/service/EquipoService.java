package com.athenet.institution.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athenet.institution.dto.request.EquipoRequest;
import com.athenet.institution.dto.response.EquipoResponse;
import com.athenet.institution.exception.ResourceNotFoundException;
import com.athenet.institution.mapper.EquipoMapper;
import com.athenet.institution.model.Deporte;
import com.athenet.institution.model.Equipo;
import com.athenet.institution.model.Sede;
import com.athenet.institution.repository.DeporteRepository;
import com.athenet.institution.repository.EquipoRepository;
import com.athenet.institution.repository.SedeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final SedeRepository sedeRepository;
    private final DeporteRepository deporteRepository;
    private final EquipoMapper equipoMapper;

    /**
     * Crea un nuevo equipo.
     * Busca primero la sede y el deporte indicados (falla si alguno no existe),
     * y con ambos en mano arma la entidad Equipo completa.
     */
    @Transactional
    public EquipoResponse crear(EquipoRequest request) {
        Sede sede = buscarSede(request.sedeId());
        Deporte deporte = buscarDeporte(request.deporteId());
        Equipo equipo = equipoMapper.toEntity(request, sede, deporte);
        return equipoMapper.toResponse(equipoRepository.save(equipo));
    }

    /**
     * Busca un equipo por su id.
     */
    public EquipoResponse obtenerPorId(Long id) {
        return equipoMapper.toResponse(buscarEntidadPorId(id));
    }

    /**
     * Devuelve todos los equipos registrados.
     */
    public List<EquipoResponse> listarTodos() {
        return equipoRepository.findAll().stream()
                .map(equipoMapper::toResponse)
                .toList();
    }

    /**
     * Devuelve los equipos que pertenecen a una sede específica.
     */
    public List<EquipoResponse> listarPorSede(Long sedeId) {
        return equipoRepository.findBySedeId(sedeId).stream()
                .map(equipoMapper::toResponse)
                .toList();
    }

    /**
     * Devuelve los equipos que practican un deporte específico,
     * pertenecientes a instituciones DISTINTAS a la indicada.
     * Permite que el ms Eventos pueda realizar
     * enfrentamientos entre clubes de instituciones diferentes.
     */
    public List<EquipoResponse> listarRivales(Long deporteId, Long institucionId) {
        return equipoRepository.findByDeporteIdAndSede_Institucion_IdNot(deporteId, institucionId).stream()
                .map(equipoMapper::toResponse)
                .toList();
    }

    /**
     * Actualiza un equipo existente, incluyendo la posibilidad
     * de reasignarlo a otra sede o deporte.
     */
    @Transactional
    public EquipoResponse actualizar(Long id, EquipoRequest request) {
        Equipo equipo = buscarEntidadPorId(id);
        Sede sede = buscarSede(request.sedeId());
        Deporte deporte = buscarDeporte(request.deporteId());
        equipoMapper.updateEntity(equipo, request, sede, deporte);
        return equipoMapper.toResponse(equipoRepository.save(equipo));
    }

    /**
     * Elimina un equipo por su id.
     */
    @Transactional
    public void eliminar(Long id) {
        Equipo equipo = buscarEntidadPorId(id);
        equipoRepository.delete(equipo);
    }

    private Equipo buscarEntidadPorId(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo con id " + id + " no fue encontrado"));
    }

    private Sede buscarSede(Long sedeId) {
        return sedeRepository.findById(sedeId)
                .orElseThrow(() -> new ResourceNotFoundException("Sede con id " + sedeId + " no fue encontrada"));
    }

    private Deporte buscarDeporte(Long deporteId) {
        return deporteRepository.findById(deporteId)
                .orElseThrow(() -> new ResourceNotFoundException("Deporte con id " + deporteId + " no fue encontrado"));
    }
}