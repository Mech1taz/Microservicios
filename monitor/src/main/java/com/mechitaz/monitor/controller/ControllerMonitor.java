package com.mechitaz.monitor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mechitaz.monitor.service.MonitorSistema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController

@RequestMapping("/api/monitor")
public class ControllerMonitor {
    @Autowired
    private final MonitorSistema monitor;
    public ControllerMonitor(MonitorSistema monitor){
        this.monitor=monitor;
    }

    @GetMapping("/status")
    public String obtenerEstado(){
        monitor.monitorearRedimiento();
        return monitor.visualizarEstado();
    }
    @GetMapping("/alertas")
    public Object obtenerAlertas(){
        return monitor.recibirAlertas();
        
    }
    
    
}
