package com.MSGFoundation.repository;

import com.MSGFoundation.model.SolicitudExterior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jacke
 */
@Repository
public interface SolicitudExteriorRepository extends JpaRepository<SolicitudExterior, Long> {
}