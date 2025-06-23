package com.mechitaz.monitor.controller;

import org.springframework.web.bind.annotation.*;

import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;
import com.mechitaz.monitor.service.MonitorSistema;

import java.util.List;
import java.util.Optional;

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
    //Para actualizar alertas
    @PutMapping("/alertas/{id}")
    public String actualizarAlerta(@PathVariable int id, @RequestBody Alerta nuevaAlerta) {
        Optional<Alerta> alertaExistente = alertaRepository.findById(id);
        if (alertaExistente.isPresent()) {
            Alerta alerta = alertaExistente.get();
            alerta.setTipo(nuevaAlerta.getTipo());
            alerta.setMensaje(nuevaAlerta.getMensaje());
            alerta.setSeveridad(nuevaAlerta.getSeveridad());
            alertaRepository.save(alerta);
            return "✅ Alerta actualizada.";
        } else {
            return "⚠️ Alerta no encontrada.";
        }
    }

    //  Eliminar una alerta por ID
    @DeleteMapping("/alertas/{id}")
    public String eliminarAlerta(@PathVariable int id) {
        if (alertaRepository.existsById(id)) {
            alertaRepository.deleteById(id);
            return "🗑 Alerta eliminada.";
        } else {
            return "⚠️ Alerta no encontrada.";
        }
    }

    // Eliminar todas las alertas
    @DeleteMapping("/alertas")
    public String eliminarTodas() {
        alertaRepository.deleteAll();
        return "🚮 Todas las alertas eliminadas.";
    }
}
