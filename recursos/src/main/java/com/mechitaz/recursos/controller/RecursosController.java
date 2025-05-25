package com.mechitaz.recursos.controller;

import com.mechitaz.recursos.service.RecursosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/recursos")
public class RecursosController {

    private final RecursosService recursosService;

    public RecursosController(RecursosService recursosService) {
        this.recursosService = recursosService;
    }

    @GetMapping("/cpu")
    public String obtenerUsoCpu() {
        return recursosService.obtenerUsoCpu();
    }

    @GetMapping("/memoria")
    public String obtenerUsoMemoria() {
        return recursosService.obtenerUsoMemoria();
    }

    @GetMapping("/disco")
    public String obtenerUsoDisco() {
        return recursosService.obtenerUsoDisco();
    }

    @GetMapping("/todo")
    public Map<String, String> obtenerTodosLosRecursos() {
        Map<String, String> recursos = new LinkedHashMap<>();
        recursos.put("cpu", recursosService.obtenerUsoCpu());
        recursos.put("memoria", recursosService.obtenerUsoMemoria());
        recursos.put("disco", recursosService.obtenerUsoDisco());
        return recursos;
    }

    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumen() {
        Map<String, Object> resumen = new LinkedHashMap<>();
        resumen.put("status", "OK");
        resumen.put("sistema", System.getProperty("os.name"));
        resumen.put("recursos", obtenerTodosLosRecursos());
        resumen.put("timestamp", System.currentTimeMillis());
        return resumen;
    }
}