package com.reservas.org.repository;

import com.reservas.org.domain.Restaurante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Restaurante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

}
