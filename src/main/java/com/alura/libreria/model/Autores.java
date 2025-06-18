package com.alura.libreria.model;

import com.alura.libreria.dtoenum.DatosAutor;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "autores")
public class Autores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libros> libroslista;

    public Autores(){}

    public Autores(DatosAutor datosAutor){
        this.nombre= datosAutor.nombre();
        this.fechaNacimiento= datosAutor.fechaNacimiento();
        this.fechaFallecimiento= datosAutor.fechaFallecimiento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libros> getLibroslista() {
        return libroslista;
    }

    public void setLibroslista(List<Libros> libroslista) {
        this.libroslista = libroslista;
    }

    @Override
    public String toString() {
        return

                ", nombre='" + nombre + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", fechaFallecimiento='" + fechaFallecimiento + '\'';
    }
}
