package com.mechitaz.monitor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;
import com.mechitaz.monitor.service.MonitorSistema;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito; //supongo que donde esta el when y doNothing estara así?
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
        when(monitor.visualizarEstado()).thenReturn("Estado del sistema: OK");

        mockMvc.perform(get("/api/monitor/status"))
               .andExpect(status().isOk())
               .andExpect(content().string("Estado del sistema: OK"));
    }

    @Test
    void testObtenerAlertas() throws Exception {
        Alerta alerta = new Alerta(1, "Sistema", "Alerta de prueba", "ALTA", null);
        when(alertaRepository.findAll()).thenReturn(List.of(alerta));

        mockMvc.perform(get("/api/monitor/alertas"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$._embedded.alertaList[0].id").value(1))
               .andExpect(jsonPath("$._embedded.alertaList[0].tipo").value("Sistema"))
               .andExpect(jsonPath("$._embedded.alertaList[0].mensaje").value("Alerta de prueba"))
               .andExpect(jsonPath("$._embedded.alertaList[0].severidad").value("ALTA"))
               .andExpect(jsonPath("$._embedded.alertaList[0]._links.self.href").exists());
    }

    @Test
    void testObtenerAlertaPorId() throws Exception {
        Alerta alerta = new Alerta(1, "Sistema", "Prueba", "MEDIA", null);
        when(alertaRepository.findById(1)).thenReturn(Optional.of(alerta));

        mockMvc.perform(get("/api/monitor/alertas/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testActualizarAlertaExistente() throws Exception {
        Alerta existente = new Alerta(1, "Sistema", "Mensaje viejo", "MEDIA", null);
        Alerta actualizada = new Alerta(1, "Sistema", "Mensaje nuevo", "BAJA", null);

        when(alertaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(alertaRepository.save(any(Alerta.class))).thenReturn(actualizada);

        mockMvc.perform(put("/api/monitor/alertas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
               .andExpect(status().isOk())
               .andExpect(content().string("Alerta actualizada."));
    }

    @Test
    void testActualizarAlertaNoExistente() throws Exception {
        when(alertaRepository.findById(99)).thenReturn(Optional.empty());

        Alerta actualizada = new Alerta(99, "Sistema", "Mensaje nuevo", "BAJA", null);

        mockMvc.perform(put("/api/monitor/alertas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
               .andExpect(status().isOk())
               .andExpect(content().string("Alerta no encontrada."));
    }

    @Test
    void testEliminarAlertaExistente() throws Exception {
        when(alertaRepository.existsById(1)).thenReturn(true);
        doNothing().when(alertaRepository).deleteById(1);

        mockMvc.perform(delete("/api/monitor/alertas/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Alerta eliminada."));
    }

    @Test
    void testEliminarAlertaNoExistente() throws Exception {
        when(alertaRepository.existsById(99)).thenReturn(false);

        mockMvc.perform(delete("/api/monitor/alertas/99"))
               .andExpect(status().isOk())
               .andExpect(content().string("Alerta no encontrada."));
    }

    @Test
    void testEliminarTodasLasAlertas() throws Exception {
        doNothing().when(alertaRepository).deleteAll();

        mockMvc.perform(delete("/api/monitor/alertas"))
               .andExpect(status().isOk())
               .andExpect(content().string("Todas las alertas eliminadas."));
    }
}
