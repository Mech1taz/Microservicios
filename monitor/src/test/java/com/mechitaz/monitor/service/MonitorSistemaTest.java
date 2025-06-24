package com.mechitaz.monitor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;

public class MonitorSistemaTest {
    @Mock
    private AlertaRepository alertaRepository;
    @InjectMocks
    private MonitorSistema monitorSistema;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVisualizarEstado() {
        String estado = monitorSistema.visualizarEstado();
        assertEquals("Desconocido", estado);
    }

    @Test
    public void testRecibirAlertas() {
        Alerta alerta = new Alerta(1, "Sistema", "Mensaje simulado", "ALTA", null);
        when(alertaRepository.findAll()).thenReturn(List.of(alerta));

        List<Alerta> resultado = monitorSistema.recibirAlertas();

        assertEquals(1, resultado.size());
        assertEquals("Mensaje simulado", resultado.get(0).getMensaje());
    }

    @Test
    public void testGuardarAlertaManual() {
        Alerta alerta = new Alerta(0, "Sistema", "Mensaje desde test", "MEDIA", null);
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alerta);

        Alerta guardada = alertaRepository.save(alerta);

        assertNotNull(guardada);
        assertEquals("MEDIA", guardada.getSeveridad());
        verify(alertaRepository, times(1)).save(alerta);
    }
}

