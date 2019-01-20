package com.reservas.org.repository;

import com.reservas.org.domain.Spot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Spot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

}
