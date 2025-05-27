package com.mechitaz.resp_and_res.controller;

import com.mechitaz.resp_and_res.model.*;
import com.mechitaz.resp_and_res.repository.*;
import com.mechitaz.resp_and_res.service.RespaldoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RespaldoController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private RespaldoService respaldoService;

    // === ESTUDIANTES ===
    @GetMapping("/estudiantes")
    public List<Estudiante> getEstudiantes() {
        return estudianteRepository.findAll();
    }

    @PostMapping("/estudiantes")
    public Estudiante saveEstudiante(@RequestBody Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    // === CURSOS ===
    @GetMapping("/cursos")
    public List<Curso> getCursos() {
        return cursoRepository.findAll();
    }

    @PostMapping("/cursos")
    public Curso saveCurso(@RequestBody Curso curso) {
        return cursoRepository.save(curso);
    }

    // === MODULOS ===
    @GetMapping("/modulos")
    public List<Modulo> getModulos() {
        return moduloRepository.findAll();
    }

    @PostMapping("/modulos")
    public Modulo saveModulo(@RequestBody Modulo modulo) {
        return moduloRepository.save(modulo);
    }

    // === EVALUACIONES ===
    @GetMapping("/evaluaciones")
    public List<Evaluacion> getEvaluaciones() {
        return evaluacionRepository.findAll();
    }

    @PostMapping("/evaluaciones")
    public Evaluacion saveEvaluacion(@RequestBody Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    // === RESPALDO ===
    @PostMapping("/respaldo/guardar")
    public String hacerRespaldo() {
        try {
            respaldoService.hacerRespaldo();
            return "Respaldo realizado correctamente :3";
        } catch (Exception e) {
            return "Error al hacer respaldo: " + e.getMessage();
        }
    }

    @PostMapping("/respaldo/restaurar")
    public String restaurar() {
        try {
            respaldoService.restaurarRespaldo();
            return "Restauración realizada correctamente :3";
        } catch (Exception e) {
            return "Error al restaurar: " + e.getMessage();
        }
    }
}