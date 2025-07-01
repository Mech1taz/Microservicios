package com.mechitaz.monitor.controller;

import org.springframework.web.bind.annotation.*;
import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;
import com.mechitaz.monitor.service.MonitorSistema;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/monitor")
@Tag(name = "Monitor API")
public class ControllerMonitor {

    @Autowired
    private MonitorSistema monitor;

    @Autowired
    private AlertaRepository alertaRepository;

    @Operation(summary = "Obtener estado del sistema")
    @GetMapping("/status")
    public String obtenerEstado() {
        monitor.monitorearRedimiento();
        return monitor.visualizarEstado();
    }

    @Operation(summary = "Lista todas las alertas")
    @GetMapping("/alertas")
    public List<Alerta> obtenerAlertas() {
        return alertaRepository.findAll();
    }

    @Operation(summary = "Obtener alerta por ID")
    @GetMapping("/alertas/{id}")
    public EntityModel<Alerta> obtenerAlertaPorId(@PathVariable int id) {
        Alerta alerta = alertaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
        
        return EntityModel.of(alerta,
            linkTo(methodOn(ControllerMonitor.class).obtenerAlertaPorId(id)).withSelfRel(),
            linkTo(methodOn(ControllerMonitor.class).obtenerAlertas()).withRel("todas-alertas"),
            linkTo(methodOn(ControllerMonitor.class).actualizarAlerta(id, null)).withRel("actualizar"),
            linkTo(methodOn(ControllerMonitor.class).eliminarAlerta(id)).withRel("eliminar")
        );
    }

    @Operation(summary = "Actualiza alerta por ID") 
    @PutMapping("/alertas/{id}")
    public String actualizarAlerta(@PathVariable int id, @RequestBody Alerta nuevaAlerta) {
        Optional<Alerta> alertaExistente = alertaRepository.findById(id);
        if (alertaExistente.isPresent()) {
            Alerta alerta = alertaExistente.get();
            alerta.setTipo(nuevaAlerta.getTipo());
            alerta.setMensaje(nuevaAlerta.getMensaje());
            alerta.setSeveridad(nuevaAlerta.getSeveridad());
            alertaRepository.save(alerta);
            return "Alerta actualizada.";
        }
        return "Alerta no encontrada.";
    }

    @Operation(summary = "Elimina alerta por ID")
    @DeleteMapping("/alertas/{id}")
    public String eliminarAlerta(@PathVariable int id) {
        if (alertaRepository.existsById(id)) {
            alertaRepository.deleteById(id);
            return "Alerta eliminada.";
        }
        return "Alerta no encontrada.";
    }

    @Operation(summary = "Elimina TODAS las alertas")
    @DeleteMapping("/alertas")
    public String eliminarTodas() {
        alertaRepository.deleteAll();
        return "Todas las alertas eliminadas.";
    }
}