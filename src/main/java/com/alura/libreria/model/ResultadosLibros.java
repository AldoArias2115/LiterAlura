package com.alura.libreria.model;

import com.alura.libreria.dtoenum.DatosLibros;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultadosLibros {

    @JsonAlias("results") List<DatosLibros> resultados;

    public List<DatosLibros> getResultados() {
        return resultados;
    }

    public void setResultados(List<DatosLibros> resultados) {
        this.resultados = resultados;
    }
}
