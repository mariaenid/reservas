package com.reservas.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Reserva.
 */
@Entity
@Table(name = "reserva")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JsonIgnoreProperties("spotreservas")
    private Restaurante restaurante;

    @ManyToOne
    @JsonIgnoreProperties("spotreservas")
    private Spot spot;

    @ManyToOne
    @JsonIgnoreProperties("clientereservas")
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Reserva fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public Reserva restaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
        return this;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Spot getSpot() {
        return spot;
    }

    public Reserva spot(Spot spot) {
        this.spot = spot;
        return this;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Reserva cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reserva reserva = (Reserva) o;
        if (reserva.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reserva.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reserva{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
