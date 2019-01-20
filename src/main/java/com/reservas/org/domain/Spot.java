package com.reservas.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Spot.
 */
@Entity
@Table(name = "spot")
public class Spot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "num_personas")
    private Integer numPersonas;

    @Column(name = "num_mesa")
    private Integer numMesa;

    @ManyToOne
    @JsonIgnoreProperties("resspots")
    private Restaurante restaurante;

    @OneToMany(mappedBy = "spot")
    private Set<Reserva> spotreservas = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumPersonas() {
        return numPersonas;
    }

    public Spot numPersonas(Integer numPersonas) {
        this.numPersonas = numPersonas;
        return this;
    }

    public void setNumPersonas(Integer numPersonas) {
        this.numPersonas = numPersonas;
    }

    public Integer getNumMesa() {
        return numMesa;
    }

    public Spot numMesa(Integer numMesa) {
        this.numMesa = numMesa;
        return this;
    }

    public void setNumMesa(Integer numMesa) {
        this.numMesa = numMesa;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public Spot restaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
        return this;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Set<Reserva> getSpotreservas() {
        return spotreservas;
    }

    public Spot spotreservas(Set<Reserva> reservas) {
        this.spotreservas = reservas;
        return this;
    }

    public Spot addSpotreserva(Reserva reserva) {
        this.spotreservas.add(reserva);
        reserva.setSpot(this);
        return this;
    }

    public Spot removeSpotreserva(Reserva reserva) {
        this.spotreservas.remove(reserva);
        reserva.setSpot(null);
        return this;
    }

    public void setSpotreservas(Set<Reserva> reservas) {
        this.spotreservas = reservas;
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
        Spot spot = (Spot) o;
        if (spot.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), spot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Spot{" +
            "id=" + getId() +
            ", numPersonas=" + getNumPersonas() +
            ", numMesa=" + getNumMesa() +
            "}";
    }
}
