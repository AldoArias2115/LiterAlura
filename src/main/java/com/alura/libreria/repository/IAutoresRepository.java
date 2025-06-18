package com.alura.libreria.repository;

import com.alura.libreria.dtoenum.DatosAutor;
import com.alura.libreria.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAutoresRepository extends JpaRepository<Autores,Long> {

    List<Autores> findAll();
    List<Autores> findByFechaNacimientoLessThanOrFechaFallecimientoGreaterThanEqual(int anioBuscado, int anioBuscado2);

}
