package cibertec.org.Consultorio_Psicologia.repository;

import cibertec.org.Consultorio_Psicologia.entity.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsicologoRepository extends JpaRepository<Psicologo, Long> {
}
