package cibertec.org.Consultorio_Psicologia.repository;

import cibertec.org.Consultorio_Psicologia.entity.HorarioDisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioDisponibleRepository  extends JpaRepository<HorarioDisponible, Long> {
}
