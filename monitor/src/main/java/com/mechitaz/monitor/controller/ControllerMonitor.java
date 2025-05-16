package com.mechitaz.monitor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mechitaz.monitor.service.MonitorSistema;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/monitor")
public class ControllerMonitor {
    private final MonitorSistema monitor = new MonitorSistema();

    @GetMapping("/api/status")
    public String obtenerEstado(){
        monitor.monitorearRedimiento();
        return monitor.visualizarEstado();
    }
    @GetMapping("/api/alertas")
    public Object obtenerAlertas(){
        return monitor.recibirAlertas();
        
    }
    
    
}
