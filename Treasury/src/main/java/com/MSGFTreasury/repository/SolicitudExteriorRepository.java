package com.MSGFTreasury.repository;

import com.MSGFTreasury.model.SolicitudExterior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jacke
 */
@Repository
public interface SolicitudExteriorRepository extends JpaRepository<SolicitudExterior, Long> {
}