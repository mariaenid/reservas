package com.reservas.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombres", length = 100)
    private String nombres;

    @Size(max = 50)
    @Column(name = "correo", length = 50)
    private String correo;

    @OneToMany(mappedBy = "cliente")
    private Set<Reserva> clientereservas = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public Cliente nombres(String nombres) {
        this.nombres = nombres;
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public Cliente correo(String correo) {
        this.correo = correo;
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Set<Reserva> getClientereservas() {
        return clientereservas;
    }

    public Cliente clientereservas(Set<Reserva> reservas) {
        this.clientereservas = reservas;
        return this;
    }

    public Cliente addClientereserva(Reserva reserva) {
        this.clientereservas.add(reserva);
        reserva.setCliente(this);
        return this;
    }

    public Cliente removeClientereserva(Reserva reserva) {
        this.clientereservas.remove(reserva);
        reserva.setCliente(null);
        return this;
    }

    public void setClientereservas(Set<Reserva> reservas) {
        this.clientereservas = reservas;
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
        Cliente cliente = (Cliente) o;
        if (cliente.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cliente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", correo='" + getCorreo() + "'" +
            "}";
    }
}
