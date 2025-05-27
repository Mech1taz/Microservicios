package com.mechitaz.resp_and_res.service;
import com.mechitaz.resp_and_res.model.*;
import com.mechitaz.resp_and_res.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class RespaldoService {

    @Autowired
    private EstudianteRepository estudianteRepo;

    @Autowired
    private CursoRepository cursoRepo;

    @Autowired
    private ModuloRepository moduloRepo;

    @Autowired
    private EvaluacionRepository evaluacionRepo;

    private final ObjectMapper mapper = new ObjectMapper();

    public void hacerRespaldo() throws IOException {
        mapper.writeValue(new File("respaldo_estudiantes.json"), estudianteRepo.findAll());
        mapper.writeValue(new File("respaldo_cursos.json"), cursoRepo.findAll());
        mapper.writeValue(new File("respaldo_modulos.json"), moduloRepo.findAll());
        mapper.writeValue(new File("respaldo_evaluaciones.json"), evaluacionRepo.findAll());
    }

    public void restaurarRespaldo() throws IOException {
        List<Curso> cursos = Arrays.asList(mapper.readValue(new File("respaldo_cursos.json"), Curso[].class));
        List<Modulo> modulos = Arrays.asList(mapper.readValue(new File("respaldo_modulos.json"), Modulo[].class));
        List<Estudiante> estudiantes = Arrays.asList(mapper.readValue(new File("respaldo_estudiantes.json"), Estudiante[].class));
        List<Evaluacion> evaluaciones = Arrays.asList(mapper.readValue(new File("respaldo_evaluaciones.json"), Evaluacion[].class));

        evaluacionRepo.deleteAll();
        moduloRepo.deleteAll();
        cursoRepo.deleteAll();
        estudianteRepo.deleteAll();

        cursoRepo.saveAll(cursos);
        moduloRepo.saveAll(modulos);
        estudianteRepo.saveAll(estudiantes);
        evaluacionRepo.saveAll(evaluaciones);
    }
}
