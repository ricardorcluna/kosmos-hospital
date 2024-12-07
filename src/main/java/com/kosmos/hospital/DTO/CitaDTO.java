package com.kosmos.hospital.DTO;

import com.kosmos.hospital.model.Consultorio;
import com.kosmos.hospital.model.Doctor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {

    @NotNull
    private Doctor doctor;

    @NotNull
    private Consultorio consultorio;

    @NotBlank
    private String nombrePaciente;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime horarioInicio;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime horarioFin;
}
