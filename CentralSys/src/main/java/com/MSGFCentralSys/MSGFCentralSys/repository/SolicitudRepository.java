package com.MSGFCentralSys.MSGFCentralSys.repository;

import com.MSGFCentralSys.MSGFCentralSys.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jacke
 */
@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}