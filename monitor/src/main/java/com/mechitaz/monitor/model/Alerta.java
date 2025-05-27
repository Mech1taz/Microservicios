package com.mechitaz.monitor.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tipo;
    private String mensaje;
    private String severidad;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaHora; 

    public boolean esCritico() {
        return "ALTA".equalsIgnoreCase(severidad);
    }

    public void marcarComoLeido() {
        this.mensaje += " (leído)";
    }
}
