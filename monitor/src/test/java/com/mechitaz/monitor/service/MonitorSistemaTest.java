package com.mechitaz.monitor.service;

import com.mechitaz.monitor.model.Alerta;
import com.mechitaz.monitor.repository.AlertaRepository;
import com.sun.management.OperatingSystemMXBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MonitorSistemaTest {

    private AlertaRepository alertaRepository;
    private OperatingSystemMXBean osBean;
    private MonitorSistema monitor;

    @BeforeEach
    void setUp() {
        alertaRepository = mock(AlertaRepository.class);
        osBean = mock(OperatingSystemMXBean.class);
        monitor = new MonitorSistema(alertaRepository, osBean);
    }

    @Test
    void testVisualizarEstadoInicial() {
        assertEquals("Desconocido", monitor.visualizarEstado());
    }

    @Test
    void testRecibirAlertas() {
        Alerta alerta = new Alerta(1, "Sistema", "CPU alta", "ALTA", null);
        when(alertaRepository.findAll()).thenReturn(List.of(alerta));

        List<Alerta> alertas = monitor.recibirAlertas();

        assertEquals(1, alertas.size());
        assertEquals("Sistema", alertas.get(0).getTipo());
    }

    @Test
    void testMonitorearRendimiento_SinSobrecarga() {
        when(osBean.getSystemCpuLoad()).thenReturn(0.3);
        when(osBean.getTotalPhysicalMemorySize()).thenReturn(16L * 1024 * 1024 * 1024); // 16 GB
        when(osBean.getFreePhysicalMemorySize()).thenReturn(4L * 1024 * 1024 * 1024); // 4 GB libres

        monitor.monitorearRedimiento();

        assertEquals("Esta estable :3", monitor.visualizarEstado());
        verify(alertaRepository, never()).save(any());
    }

    @Test
    void testMonitorearRendimiento_ConSobrecargaCPU() {
        when(osBean.getSystemCpuLoad()).thenReturn(0.9); // 90%
        when(osBean.getTotalPhysicalMemorySize()).thenReturn(16L * 1024 * 1024 * 1024); // 16 GB
        when(osBean.getFreePhysicalMemorySize()).thenReturn(4L * 1024 * 1024 * 1024); // 4 GB libres

        monitor.monitorearRedimiento();

        assertEquals("SOBRECARGAAAAAAAAA", monitor.visualizarEstado());
        verify(alertaRepository, times(1)).save(any(Alerta.class));
    }

    @Test
    void testMonitorearRendimiento_ConSobrecargaRAM() {
        when(osBean.getSystemCpuLoad()).thenReturn(0.2);
        when(osBean.getTotalPhysicalMemorySize()).thenReturn(16L * 1024 * 1024 * 1024); // 16 GB
        when(osBean.getFreePhysicalMemorySize()).thenReturn(2L * 1024 * 1024 * 1024); // solo 2 GB libres 

        monitor.monitorearRedimiento();

        assertEquals("SOBRECARGAAAAAAAAA", monitor.visualizarEstado());
        verify(alertaRepository).save(any(Alerta.class));
    }
}
