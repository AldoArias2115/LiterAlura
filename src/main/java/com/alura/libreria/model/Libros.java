package com.alura.libreria.model;


import com.alura.libreria.dtoenum.DatosLibros;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // Guardamos el autor
    @JoinColumn(name = "autor_id")
    private Autores autor;
    private String idiomas;
    private Double numDescargas;


    public Libros(){
    }

    public Libros (DatosLibros datosLibros){

        this.titulo= datosLibros.titulo();
        if (datosLibros.autor() != null && !datosLibros.autor().isEmpty()) {
            this.autor = new Autores(datosLibros.autor().get(0));
        } else {
            this.autor = null; //
        }
        this.idiomas= idiomasLibro(datosLibros.idiomas());
        this.numDescargas= datosLibros.numDescargas();
    }


    private String idiomasLibro(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "Desconocido";
        }
        return idiomas.get(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autores getAutor() {
        return autor;
    }

    public void setAutor(Autores autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumDescargas() {
        return numDescargas;
    }

    public void setNumDescargas(Double numDescargas) {
        this.numDescargas = numDescargas;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + (autor != null ? autor.getNombre() : "N/A") +
                ", idiomas='" + idiomas +
                ", numDescargas=" + numDescargas;
    }
}
