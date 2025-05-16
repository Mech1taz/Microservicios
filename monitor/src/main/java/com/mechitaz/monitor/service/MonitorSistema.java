package com.mechitaz.monitor.service;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mechitaz.monitor.model.Alerta;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class MonitorSistema {
    private String estado= "Desconocido";
    private List<Alerta> alertas = new ArrayList<>();

    public String visualizarEstado(){
        return estado;
    }
    public List<Alerta> recibirAlertas(){
        return alertas;
    }

    public void monitorearRedimiento(){
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpu = osBean.getSystemCpuLoad();
        long total = Runtime.getRuntime().totalMemory();
        long libre = Runtime.getRuntime().freeMemory();
        long usada = total - libre;
        
        boolean sobrecarga = cpu >0.8 || usada >total *0.8;
        if (sobrecarga){
            this.estado  ="SOBRECARGAAAAAAAA";
            alertas.add(new Alerta(
                alertas.size()+1,
                "Sistema",
                "El sistema está sobrecargado (CPU): "+String.format("%.2f",cpu * 100)+"%, RAM usada: "+ (usada/1024/1024) + "MB)",
                "ALTA"
                ));
        }else{
            this.estado = "TABIEN :3";
            
        }
    }
}
