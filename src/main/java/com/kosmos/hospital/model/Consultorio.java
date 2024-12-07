package com.kosmos.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultorio {

    @Id
    @Column(name = "consultorio_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultorioId;

    @NotNull
    private Integer numeroConsultorio;

    @NotNull
    private Integer piso;
}
