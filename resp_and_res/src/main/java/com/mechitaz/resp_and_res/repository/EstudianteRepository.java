package com.mechitaz.resp_and_res.repository;
import com.mechitaz.resp_and_res.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}