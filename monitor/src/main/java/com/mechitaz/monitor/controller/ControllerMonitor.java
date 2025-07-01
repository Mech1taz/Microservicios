package com.mechitaz.monitor.controller;

import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;
import com.mechitaz.monitor.service.MonitorSistema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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

    @Operation(summary = "Lista todas las alertas con enlaces HATEOAS")
    @GetMapping("/alertas")
    public ResponseEntity<CollectionModel<EntityModel<Alerta>>> obtenerAlertas() {
        List<Alerta> alertas = alertaRepository.findAll();

        List<EntityModel<Alerta>> recursos = alertas.stream()
            .map(alerta -> EntityModel.of(alerta,
                linkTo(methodOn(ControllerMonitor.class).obtenerAlerta(alerta.getId())).withSelfRel(),
                linkTo(methodOn(ControllerMonitor.class).obtenerAlertas()).withRel("todasLasAlertas")))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(recursos,
                linkTo(methodOn(ControllerMonitor.class).obtenerAlertas()).withSelfRel()));
    }

    @Operation(summary = "Obtiene una alerta por ID")
    @GetMapping("/alertas/{id}")
    public ResponseEntity<EntityModel<Alerta>> obtenerAlerta(@PathVariable int id) {
        return alertaRepository.findById(id)
            .map(alerta -> EntityModel.of(alerta,
                linkTo(methodOn(ControllerMonitor.class).obtenerAlerta(id)).withSelfRel(),
                linkTo(methodOn(ControllerMonitor.class).obtenerAlertas()).withRel("todasLasAlertas")))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualiza alerta por ID")
    @PutMapping("/alertas/{id}")
    public ResponseEntity<String> actualizarAlerta(@PathVariable int id, @RequestBody Alerta nuevaAlerta) {
        Optional<Alerta> alertaExistente = alertaRepository.findById(id);
        if (alertaExistente.isPresent()) {
            Alerta alerta = alertaExistente.get();
            alerta.setTipo(nuevaAlerta.getTipo());
            alerta.setMensaje(nuevaAlerta.getMensaje());
            alerta.setSeveridad(nuevaAlerta.getSeveridad());
            alertaRepository.save(alerta);
            return ResponseEntity.ok("Alerta actualizada.");
        }
        return ResponseEntity.ok("Alerta no encontrada.");
    }

    @Operation(summary = "Elimina alerta por ID")
    @DeleteMapping("/alertas/{id}")
    public ResponseEntity<String> eliminarAlerta(@PathVariable int id) {
        if (alertaRepository.existsById(id)) {
            alertaRepository.deleteById(id);
            return ResponseEntity.ok("Alerta eliminada.");
        }
        return ResponseEntity.ok("Alerta no encontrada.");
    }

    @Operation(summary = "Elimina TODAS las alertas")
    @DeleteMapping("/alertas")
    public ResponseEntity<String> eliminarTodas() {
        alertaRepository.deleteAll();
        return ResponseEntity.ok("Todas las alertas eliminadas.");
    }
}
