package com.mechitaz.monitor.service;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;

@Service
public class MonitorSistema {

    private String estado = "Desconocido";
    private final List<Alerta> alertas = new ArrayList<>();

    @Autowired
    private AlertaRepository alertaRepository;

    public String visualizarEstado() {
        return estado;
    }

    public List<Alerta> recibirAlertas() {
        return alertas;
    }

    @SuppressWarnings("deprecation")
    public void monitorearRedimiento() {
    OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    double cpu = osBean.getSystemCpuLoad();  // aún puede dar -1

    long totalRam = osBean.getTotalPhysicalMemorySize();
    long freeRam = osBean.getFreePhysicalMemorySize();
    long usedRam = totalRam - freeRam;

    boolean sobrecarga = false;

    // Solo verificar si el CPU es válido
    if (cpu >= 0) {
        sobrecarga = cpu > 0.8;
    }

    // Verificar si se está usando más del 80% de RAM física
    sobrecarga = sobrecarga || usedRam > totalRam * 0.8;

    if (sobrecarga) {
        this.estado = "SOBRECARGAAAAAAAAAAAAAAAAAAAAAAAAAAA";

        Alerta alerta = new Alerta(
            0,
            "Sistema",
            "CPU: " + (cpu >= 0 ? String.format("%.2f", cpu * 100) + "%" : "No disponible") +
            ", RAM usada: " + (usedRam / 1024 / 1024) + " MB",
            "ALTA"
        );

        alertas.add(alerta);
        alertaRepository.save(alerta);
    } else {
        this.estado = "Esta bien :3";
    }
    }

}
