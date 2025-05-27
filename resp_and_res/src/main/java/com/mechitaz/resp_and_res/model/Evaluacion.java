package com.mechitaz.resp_and_res.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Estudiante estudiante;

    @ManyToOne
    private Curso curso;

    @ManyToOne
    private Modulo modulo;

    private Double nota;
}