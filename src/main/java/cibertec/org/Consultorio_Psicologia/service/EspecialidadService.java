package cibertec.org.Consultorio_Psicologia.service;

import cibertec.org.Consultorio_Psicologia.entity.Especialidad;
import cibertec.org.Consultorio_Psicologia.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService {
    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> listarTodos() {
        return especialidadRepository.findAll();
    }

    public Optional<Especialidad> buscarPorId(Long id) {
        return especialidadRepository.findById(id);
    }

    public Especialidad guardar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    public void eliminar(Long id) {
        especialidadRepository.deleteById(id);
    }
}
