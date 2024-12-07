package com.kosmos.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "cita_id")
    private Long citaId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "consultorio_id", nullable = false)
    private Consultorio consultorio;

    @NotBlank
    private String nombrePaciente;

    @NotNull
    private LocalDateTime horarioInicio;

    @NotNull
    private LocalDateTime horarioFin;
}
