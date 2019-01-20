package com.reservas.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Restaurante.
 */
@Entity
@Table(name = "restaurante")
public class Restaurante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Size(max = 100)
    @Column(name = "propietario", length = 100)
    private String propietario;

    @Size(max = 500)
    @Column(name = "direccion", length = 500)
    private String direccion;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;

    @Column(name = "imagen_content_type")
    private String imagenContentType;

    @OneToMany(mappedBy = "restaurante")
    private Set<Spot> resspots = new HashSet<>();
    @OneToMany(mappedBy = "restaurante")
    private Set<Reserva> spotreservas = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Restaurante nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Restaurante fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getPropietario() {
        return propietario;
    }

    public Restaurante propietario(String propietario) {
        this.propietario = propietario;
        return this;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getDireccion() {
        return direccion;
    }

    public Restaurante direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public Restaurante imagen(byte[] imagen) {
        this.imagen = imagen;
        return this;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return imagenContentType;
    }

    public Restaurante imagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
        return this;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public Set<Spot> getResspots() {
        return resspots;
    }

    public Restaurante resspots(Set<Spot> spots) {
        this.resspots = spots;
        return this;
    }

    public Restaurante addResspot(Spot spot) {
        this.resspots.add(spot);
        spot.setRestaurante(this);
        return this;
    }

    public Restaurante removeResspot(Spot spot) {
        this.resspots.remove(spot);
        spot.setRestaurante(null);
        return this;
    }

    public void setResspots(Set<Spot> spots) {
        this.resspots = spots;
    }

    public Set<Reserva> getSpotreservas() {
        return spotreservas;
    }

    public Restaurante spotreservas(Set<Reserva> reservas) {
        this.spotreservas = reservas;
        return this;
    }

    public Restaurante addSpotreserva(Reserva reserva) {
        this.spotreservas.add(reserva);
        reserva.setRestaurante(this);
        return this;
    }

    public Restaurante removeSpotreserva(Reserva reserva) {
        this.spotreservas.remove(reserva);
        reserva.setRestaurante(null);
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
        Restaurante restaurante = (Restaurante) o;
        if (restaurante.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), restaurante.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Restaurante{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", propietario='" + getPropietario() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", imagenContentType='" + getImagenContentType() + "'" +
            "}";
    }
}
