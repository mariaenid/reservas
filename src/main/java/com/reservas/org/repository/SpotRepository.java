package com.reservas.org.repository;

import com.reservas.org.domain.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import com.reservas.org.domain.Restaurante;
import java.util.List;


/**
 * Spring Data  repository for the Spot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    List<Spot> findByRestauranteId(long id);


}
