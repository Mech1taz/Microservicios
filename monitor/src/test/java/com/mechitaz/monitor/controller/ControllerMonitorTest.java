package com.mechitaz.monitor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;
import com.mechitaz.monitor.service.MonitorSistema;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerMonitor.class)
public class ControllerMonitorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitorSistema monitor;

    @MockBean
    private AlertaRepository alertaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testObtenerEstado() throws Exception {
        when(monitor.visualizarEstado()).thenReturn("SOBRECARGAAAAAAAAA");

        mockMvc.perform(get("/api/monitor/status"))
               .andExpect(status().isOk())
               .andExpect(content().string("SOBRECARGAAAAAAAAA"));
    }

    @Test
    void testObtenerAlertas() throws Exception {
        Alerta alerta = new Alerta(1, "Sistema", "Alerta de prueba", "ALTA", null);
        when(monitor.recibirAlertas()).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/monitor/alertas"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].tipo").value("Sistema"));
    }

    @Test
    void testActualizarAlerta() throws Exception {
        Alerta existente = new Alerta(1, "Sistema", "Mensaje viejo", "MEDIA", null);
        Alerta actualizada = new Alerta(1, "Sistema", "Mensaje nuevo", "BAJA", null);

        when(alertaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(alertaRepository.save(any(Alerta.class))).thenReturn(actualizada);

        mockMvc.perform(put("/api/monitor/alertas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
               .andExpect(status().isOk())
               .andExpect(content().string("✅ Alerta actualizada."));
    }

    @Test
    void testEliminarAlerta() throws Exception {
        when(alertaRepository.existsById(1)).thenReturn(true);
        doNothing().when(alertaRepository).deleteById(1);

        mockMvc.perform(delete("/api/monitor/alertas/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("🗑 Alerta eliminada."));
    }

    @Test
    void testEliminarTodasLasAlertas() throws Exception {
        doNothing().when(alertaRepository).deleteAll();

        mockMvc.perform(delete("/api/monitor/alertas"))
               .andExpect(status().isOk())
               .andExpect(content().string("🚮 Todas las alertas eliminadas."));
    }
}