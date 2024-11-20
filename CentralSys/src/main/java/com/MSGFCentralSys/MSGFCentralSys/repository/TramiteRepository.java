package com.MSGFCentralSys.MSGFCentralSys.repository;

import com.MSGFCentralSys.MSGFCentralSys.model.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jacke
 */
@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Long> {
}