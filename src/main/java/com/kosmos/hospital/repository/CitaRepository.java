package com.kosmos.hospital.repository;

import com.kosmos.hospital.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByDoctorDoctorIdAndHorarioInicioBetween(Long doctorId, LocalDateTime inicio, LocalDateTime fin);
    List<Cita> findByConsultorioConsultorioIdAndHorarioInicioBetween(Long consultorioId, LocalDateTime inicio, LocalDateTime fin);
    List<Cita> findByNombrePacienteAndHorarioInicioBetween(String nombrePaciente, LocalDateTime inicio, LocalDateTime fin);


    @Query("SELECT COUNT(c) FROM Cita c " +
            "WHERE c.doctor.doctorId = :doctorId " +
            "AND c.horarioInicio BETWEEN :inicio AND :fin")
    Long countCitasByDoctorInDateRange(@Param("doctorId") Long doctorId,
                                       @Param("inicio") LocalDateTime inicio,
                                       @Param("fin") LocalDateTime fin);

    @Query("SELECT c FROM Cita c " +
            "WHERE c.citaId = :citaId " +
            "AND c.horarioInicio > CURRENT_TIMESTAMP")
    Optional<Cita> findCancelableCita(@Param("citaId") Long citaId);

}
