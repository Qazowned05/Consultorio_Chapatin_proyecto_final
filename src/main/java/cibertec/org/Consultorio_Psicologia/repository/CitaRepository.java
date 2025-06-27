package cibertec.org.Consultorio_Psicologia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cibertec.org.Consultorio_Psicologia.entity.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPsicologo_Id(Long psicologoId);
    
    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.psicologo p LEFT JOIN FETCH p.especialidad ORDER BY c.fecha DESC")
    List<Cita> findAllWithDetails();
    
    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.psicologo p LEFT JOIN FETCH p.especialidad WHERE c.id = :id")
    Optional<Cita> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.psicologo p LEFT JOIN FETCH p.especialidad WHERE c.paciente.id = :pacienteId ORDER BY c.fecha DESC")
    List<Cita> findByPaciente_IdWithDetails(@Param("pacienteId") Long pacienteId);
}
