package cibertec.org.Consultorio_Psicologia.service;

import cibertec.org.Consultorio_Psicologia.entity.HorarioDisponible;
import cibertec.org.Consultorio_Psicologia.repository.HorarioDisponibleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioDisponibleService {
    @Autowired
    private HorarioDisponibleRepository horarioRepository;

    public List<HorarioDisponible> listarTodos() {
        return horarioRepository.findAll();
    }

    public Optional<HorarioDisponible> buscarPorId(Long id) {
        return horarioRepository.findById(id);
    }

    public HorarioDisponible guardar(HorarioDisponible horario) {
        return horarioRepository.save(horario);
    }

    public void eliminar(Long id) {
        horarioRepository.deleteById(id);
    }
}
