package com.mechitaz.resp_and_res.repository;
import com.mechitaz.resp_and_res.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}