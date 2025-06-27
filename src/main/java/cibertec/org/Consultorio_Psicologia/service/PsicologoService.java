package cibertec.org.Consultorio_Psicologia.service;

import cibertec.org.Consultorio_Psicologia.entity.Psicologo;
import cibertec.org.Consultorio_Psicologia.repository.PsicologoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PsicologoService {
    @Autowired
    private PsicologoRepository psicologoRepository;

    public List<Psicologo> listarTodos() {
        return psicologoRepository.findAll();
    }

    public Optional<Psicologo> buscarPorId(Long id) {
        return psicologoRepository.findById(id);
    }

    public Psicologo guardar(Psicologo psicologo) {
        return psicologoRepository.save(psicologo);
    }

    public void eliminar(Long id) {
        psicologoRepository.deleteById(id);
    }
}
