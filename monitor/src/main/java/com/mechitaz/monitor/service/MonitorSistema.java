package com.mechitaz.monitor.service;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;

@Service
public class MonitorSistema {

    private String estado = "Desconocido";

    private AlertaRepository alertaRepository;
    private OperatingSystemMXBean osBean;

    // Constructor 
    @Autowired
    public MonitorSistema(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
        this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    // Constructor alternativo para pruebas unitarias
    public MonitorSistema(AlertaRepository alertaRepository, OperatingSystemMXBean osBean) {
        this.alertaRepository = alertaRepository;
        this.osBean = osBean;
    }

    public String visualizarEstado() {
        return estado;
    }

    public List<Alerta> recibirAlertas() {
        return alertaRepository.findAll();
    }

    
    public void monitorearRedimiento() {
        double cpu = osBean.getSystemCpuLoad();
        long totalRam = osBean.getTotalPhysicalMemorySize();
        long freeRam = osBean.getFreePhysicalMemorySize();
        long usedRam = totalRam - freeRam;

        boolean sobrecarga = false;

        if (cpu >= 0) {
            sobrecarga = cpu > 0.8;
        }

        sobrecarga = sobrecarga || usedRam > totalRam * 0.8;

        if (sobrecarga) {
            estado = "SOBRECARGAAAAAAAAA";

            String mensaje = "CPU: " + (cpu >= 0 ? String.format("%.2f", cpu * 100) + "%" : "No disponible") +
                            ", RAM usada: " + (usedRam / 1024 / 1024) + " MB";

            Alerta alerta = new Alerta(0, "Sistema", mensaje, "ALTA", null);
            alertaRepository.save(alerta);
        } else {
            estado = "Esta estable :3";
        }
    }

    

}
