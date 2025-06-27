package cibertec.org.Consultorio_Psicologia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cibertec.org.Consultorio_Psicologia.entity.Cita;
import cibertec.org.Consultorio_Psicologia.repository.CitaRepository;

@Service
public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    @Transactional(readOnly = true)
    public List<Cita> listarTodos() {
        return citaRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    public Optional<Cita> buscarPorId(Long id) {
        return citaRepository.findById(id);
    }

    @Transactional
    public Cita guardar(Cita cita) {
        return citaRepository.save(cita);
    }

    @Transactional
    public void eliminar(Long id) {
        citaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Cita> buscarPorPsicologoId(Long psicologoId) {
        return citaRepository.findByPsicologo_Id(psicologoId);
    }

    @Transactional(readOnly = true)
    public List<Cita> buscarPorPacienteIdConDetalles(Long pacienteId) {
        return citaRepository.findByPaciente_IdWithDetails(pacienteId);
    }
}
