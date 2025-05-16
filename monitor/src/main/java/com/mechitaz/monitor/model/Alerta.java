package com.mechitaz.monitor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tipo;
    private String mensaje;
    private String severidad;


public boolean esCritico(){
    return "ALTA".equalsIgnoreCase(severidad);
}
public void marcarComoLeido(){
    this.mensaje += "(leido)";
}
}