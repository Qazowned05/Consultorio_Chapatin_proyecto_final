package cibertec.org.Consultorio_Psicologia.repository;

import cibertec.org.Consultorio_Psicologia.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByemail(String email);
}
