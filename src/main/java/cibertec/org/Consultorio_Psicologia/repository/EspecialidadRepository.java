package cibertec.org.Consultorio_Psicologia.repository;

import cibertec.org.Consultorio_Psicologia.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
}
