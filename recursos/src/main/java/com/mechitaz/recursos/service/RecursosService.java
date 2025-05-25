package com.mechitaz.recursos.service;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

@Service
public class RecursosService {
    private final DecimalFormat df = new DecimalFormat("0.00");

    public String obtenerUsoCpu() {
        try {
            OperatingSystemMXBean osBean = (OperatingSystemMXBean) 
                ManagementFactory.getOperatingSystemMXBean();
            double cpuLoad = osBean.getSystemCpuLoad() * 100;
            return df.format(cpuLoad) + "%";
        } catch (Exception e) {
            return "N/A";
        }
    }

    public String obtenerUsoMemoria() {
        try {
            OperatingSystemMXBean osBean = (OperatingSystemMXBean) 
                ManagementFactory.getOperatingSystemMXBean();
            long totalMB = osBean.getTotalPhysicalMemorySize() / (1024 * 1024);
            long freeMB = osBean.getFreePhysicalMemorySize() / (1024 * 1024);
            return freeMB + "MB libres de " + totalMB + "MB";
        } catch (Exception e) {
            return "N/A";
        }
    }

    public String obtenerUsoDisco() {
    try {
        // Ejecutar comando WMIC para obtener información del disco C:
        Process process = Runtime.getRuntime().exec(
            "wmic logicaldisk where DeviceID='C:' get Size,FreeSpace");
        
        // Leer la salida del comando
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
        
        String line;
        long size = 0;
        long freeSpace = 0;
        
        // Procesar la salida
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            
            // Extraer números (eliminar todos los caracteres no numéricos)
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 2 && parts[0].matches("\\d+")) {
                freeSpace = Long.parseLong(parts[0]);
                size = Long.parseLong(parts[1]);
                break;
            }
        }
        
        // Convertir a GB
        double freeGB = freeSpace / (1024.0 * 1024.0 * 1024.0);
        double totalGB = size / (1024.0 * 1024.0 * 1024.0);
        
        return String.format("%.2fGB libres de %.2fGB", freeGB, totalGB);
    } catch (Exception e) {
        return "Error al leer disco: " + e.getMessage();
    }
}
}