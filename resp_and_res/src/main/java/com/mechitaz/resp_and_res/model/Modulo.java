package com.mechitaz.resp_and_res.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modulo {
    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    @ManyToOne
    private Curso curso;
}