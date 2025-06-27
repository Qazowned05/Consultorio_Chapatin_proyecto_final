package cibertec.org.Consultorio_Psicologia.service;

import cibertec.org.Consultorio_Psicologia.entity.Usuario;
import cibertec.org.Consultorio_Psicologia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByemail(email);
    }
}
