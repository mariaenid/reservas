package com.reservas.org.repository;

import com.reservas.org.domain.Reserva;
import com.reservas.org.domain.Restaurante;
import com.reservas.org.domain.Spot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


/**
 * Spring Data  repository for the Reserva entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Reserva findByRestauranteIdAndSpotIdAndFecha(Long idRestaurante, Long idSpot, LocalDate fecha);
}
