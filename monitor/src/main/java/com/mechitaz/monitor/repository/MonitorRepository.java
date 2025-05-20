package com.mechitaz.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mechitaz.monitor.model.Alerta;

@Repository
public interface MonitorRepository extends JpaRepository<Alerta, Integer>{
    
}
