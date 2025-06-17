package com.mechitaz.monitor.controller;

import org.springframework.web.bind.annotation.*;

import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;
import com.mechitaz.monitor.service.MonitorSistema;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/monitor")
public class ControllerMonitor {

    @Autowired
    private MonitorSistema monitor;

    @Autowired
    private AlertaRepository alertaRepository;

    // Monitorea el sistema y devuelve el estado actual
    @GetMapping("/status")
    public String obtenerEstado() {
        monitor.monitorearRedimiento();
        return monitor.visualizarEstado();
    }

    // Devuelve todas las alertas
    @GetMapping("/alertas")
    public List<Alerta> obtenerAlertas() {
        return monitor.recibirAlertas();
    }
    
}
