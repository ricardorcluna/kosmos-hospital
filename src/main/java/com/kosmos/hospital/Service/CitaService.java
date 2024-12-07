package com.kosmos.hospital.Service;

import com.kosmos.hospital.DTO.CitaDTO;
import com.kosmos.hospital.model.Cita;
import com.kosmos.hospital.repository.CitaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Transactional
    @Modifying
    public Cita agendarCita(CitaDTO citaDTO) {

        Cita cita = convertirCitaDTOaCita(citaDTO);

        validarHorarios(cita);
        validaConclictosConsultorio(cita);
        validarConflictosDoctor(cita);
        validarConflictosPaciente(cita);
        validarLimiteCitasDoctor(cita);

        // Guardar la cita si pasa todas las validaciones
        return citaRepository.save(cita);
    }

    @Transactional
    public void cancelarCita(Long citaId) {
        Cita cita = citaRepository.findCancelableCita(citaId)
                .orElseThrow(() -> new RuntimeException("La cita no puede ser cancelada o no existe."));
        citaRepository.delete(cita);
    }

    @Transactional
    public void editarCita(Long citaId,  CitaDTO citaDTO) {
        Cita citaExistente = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("La cita no existe."));

        Cita nuevaCita = convertirCitaDTOaCita(citaDTO);

        validarHorarios(nuevaCita);
        validaConclictosConsultorio(nuevaCita);
        validarConflictosDoctor(nuevaCita);
        validarConflictosPaciente(nuevaCita);
        validarLimiteCitasDoctor(nuevaCita);

        // Actualizar datos de la cita
        citaExistente.setConsultorio(nuevaCita.getConsultorio());
        citaExistente.setNombrePaciente(nuevaCita.getNombrePaciente());
        citaExistente.setHorarioInicio(nuevaCita.getHorarioInicio());
        citaExistente.setHorarioFin(nuevaCita.getHorarioFin());

        citaRepository.save(citaExistente);
    }


    // Validación de que el horario de inicio es antes del horario de fin
    public void validarHorarios(Cita cita){
        if (!cita.getHorarioInicio().isBefore(cita.getHorarioFin())) {
            throw new RuntimeException("El horario de inicio debe ser antes del horario de fin.");
        }
    }

    //Valida que no haya conflictos de horario en el consultorio.
    public void validaConclictosConsultorio(Cita cita){
        if (!citaRepository.findByConsultorioConsultorioIdAndHorarioInicioBetween(
                cita.getConsultorio().getConsultorioId(),
                cita.getHorarioInicio(),
                cita.getHorarioFin()).isEmpty()) {
            throw new RuntimeException("El consultorio ya tiene una cita en este horario.");
        }
    }

    // Validar que el doctor no tenga otra cita en el mismo horario
    private void validarConflictosDoctor(Cita cita) {
        if (!citaRepository.findByDoctorDoctorIdAndHorarioInicioBetween(
                cita.getDoctor().getDoctorId(), cita.getHorarioInicio(), cita.getHorarioFin()).isEmpty()) {
            throw new RuntimeException("El doctor ya tiene una cita en este horario.");
        }
    }

    //Valida que el paciente no tenga otra cita en el mismo día con menos de 2 horas de diferencia.
    private void validarConflictosPaciente(Cita cita) {
        LocalDateTime inicioDelDia = cita.getHorarioInicio().toLocalDate().atStartOfDay();
        LocalDateTime finDelDia = cita.getHorarioInicio().toLocalDate().atTime(23, 59, 59);

        List<Cita> citasPaciente = citaRepository.findByNombrePacienteAndHorarioInicioBetween(
                cita.getNombrePaciente(), inicioDelDia, finDelDia);

        for (Cita citaExistente : citasPaciente) {
            LocalDateTime existenteFin = citaExistente.getHorarioFin();
            if (cita.getHorarioInicio().isBefore(existenteFin.plusHours(2)) &&
                    cita.getHorarioFin().isAfter(citaExistente.getHorarioInicio().minusHours(2))) {
                throw new RuntimeException("El paciente ya tiene una cita dentro de las 2 horas de diferencia.");
            }
        }
    }

    //Valida que el dcotor no tenga más de 8 citas al día
    private void validarLimiteCitasDoctor(Cita cita) {
        if (citaRepository.countCitasByDoctorInDateRange(
                cita.getDoctor().getDoctorId(),
                cita.getHorarioInicio().toLocalDate().atStartOfDay(),
                cita.getHorarioInicio().toLocalDate().atTime(LocalTime.MAX)) > 8   ) {
            throw new RuntimeException("El doctor ya se encuentra limite de citas.");
        }
    }

    private Cita convertirCitaDTOaCita(CitaDTO citaRequest) {
        Cita cita = new Cita();
        cita.setDoctor(citaRequest.getDoctor());
        cita.setConsultorio(citaRequest.getConsultorio());
        cita.setNombrePaciente(citaRequest.getNombrePaciente());
        cita.setHorarioInicio(citaRequest.getHorarioInicio());
        cita.setHorarioFin(citaRequest.getHorarioFin());
        return cita;
    }
}
