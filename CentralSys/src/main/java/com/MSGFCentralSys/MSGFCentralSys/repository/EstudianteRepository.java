package com.MSGFCentralSys.MSGFCentralSys.repository;

import com.MSGFCentralSys.MSGFCentralSys.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jacke
 */
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}