package com.alura.libreria.repository;

import com.alura.libreria.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILibrosRepository extends JpaRepository<Libros,Long> {

    boolean existsByTitulo(String titulo);
    List<Libros> findByIdiomas(String idiomas);
}
