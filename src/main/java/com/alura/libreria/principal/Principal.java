package com.alura.libreria.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import com.alura.libreria.dtoenum.DatosLibros;
import com.alura.libreria.model.Autores;
import com.alura.libreria.model.Libros;
import com.alura.libreria.model.ResultadosLibros;
import com.alura.libreria.repository.IAutoresRepository;
import com.alura.libreria.repository.ILibrosRepository;
import com.alura.libreria.service.ConsumoApi;
import com.alura.libreria.service.ConvierteDatos;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private Scanner entrada = new Scanner(System.in);
    private Scanner menu = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libros> datosLibro = new ArrayList<>();
    int opcion = 0;

    private ILibrosRepository librosRepositorio;
    private IAutoresRepository autoresRepositorio;

    public Principal(ILibrosRepository librosRepository, IAutoresRepository iAutoresRepository) {
        this.librosRepositorio = librosRepository;
        this.autoresRepositorio = iAutoresRepository;

    }

    public void mostrarMenu() {

        try {
            do {
                System.out.println("""
                        **************************************************
                        Bienvenido a nuestro conversor de monedas.

                        Elija una de las siguientes opciones para realizar una conversion.

                        1.- Buscar Libro en la web
                        2.- Listar Libros Registrados
                        3.- Listar Autores Registrados
                        4.- Listar Autores Vivos En Un Determinado Año
                        5.- Listar Libros Por Idioma
                        0.- Salir

                        **************************************************
                        """);
                if (menu.hasNextInt()) {
                    opcion = menu.nextInt();

                    switch (opcion) {
                        case 1:
                            buscarLibro();
                            break;

                        case 2:
                            listarLibros();
                            break;

                        case 3:
                            mostrarAutoresRegistrados();
                            break;

                        case 4:
                            buscarAutoresXAnio();
                            break;

                        case 5:
                            listarLibrosXIdioma();
                            break;

                        case 0:
                            System.out.println("Cerrando la aplicacion");
                            break;
                        default:
                            System.out.println("Opción no válida. Intenta de nuevo.");
                    }
                } else {
                    System.out.println("Entrada inválida. Por favor ingresa un número.");
                    menu.next();
                }

            } while (opcion != 0);

        } catch (RuntimeException e) {
            System.err.println("Ocurrió un error durante la consulta: " + e.getMessage());
        }
        menu.close();

    }

    // Obteniendo el libro desde la API
    private Libros getDatosLibros() {

        System.out.println("Ingresa el nombre de libro a buscar");
        var tituloLibro = entrada.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "%20"));

        var datosBusqueda = conversor.obtenerDatos(json, ResultadosLibros.class);

        if (datosBusqueda != null && datosBusqueda.getResultados() != null
                && !datosBusqueda.getResultados().isEmpty()) {
            DatosLibros obtenerLibro = datosBusqueda.getResultados().get(0); // Obtener el primer libro de la lista
            return new Libros(obtenerLibro);
        } else {
            System.out.println("No se encontraron resultados.");
            return null;
        }
    }

    private void buscarLibro() {
        // Conectamos primero la busqueda de la api y llamar el metodo que obtiene el
        // libro desde la Api
        Libros libro = getDatosLibros();

        if (libro == null) {
            System.out.println("Libro no encontrado. el valor es null");
            return;
        }
        // agregando el libro a la base de datos
        try {
            boolean libroExiste = librosRepositorio.existsByTitulo(libro.getTitulo());
            if (libroExiste) {
                System.out.println("El libro ya existe en la base de datos!");
            } else {
                librosRepositorio.save(libro);
                System.out.println("El libro " + libro + " se ha guardado exitosamente");
            }
        } catch (InvalidDataAccessApiUsageException e) {
            System.out.println("Ha ocurrido un error al guardar el libro: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    private void listarLibros() {
        List<Libros> libros = librosRepositorio.findAll();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
            return;

        } else {
            System.out.println("Libros encontrados en la base de datos: \n");
            for (Libros libro : libros) {
                System.out.println("Título: " + libro.getTitulo());

                Autores autor = libro.getAutor();

                if (autor != null) {
                    System.out.println("Autor: " + autor.getNombre());
                    System.out.println("Nacimiento: " + autor.getFechaNacimiento());
                    System.out.println("Fallecimiento: " + autor.getFechaFallecimiento());
                    System.out.println("Idioma:" + libro.getIdiomas());
                    System.out.println("-------------");
                }

                else {
                    System.out.println("Autor: Desconocido");
                }
            }
        }

    }

    private void mostrarAutoresRegistrados() {
        List<Autores> autores = autoresRepositorio.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores registrados en la base de datos.\n");
            return;
        }

        System.out.println("Los autores registrados en la base de datos son los siguientes:\n");

        autores.stream()
                .distinct()
                .forEach(autor -> {
                    System.out.println("Autor: " + autor.getNombre());
                    List<Libros> libros = autor.getLibroslista();
                    if (libros == null || libros.isEmpty()) {
                        System.out.println("Libros: No tiene libros registrados.");
                    } else {
                        libros.forEach(libro -> System.out
                                .println("El siguiente libro es una de sus obras: " + libro.getTitulo()
                                        + "\n---------------------------------"));
                    }
                });

    }

    private void buscarAutoresXAnio() {

        System.out.println("Indica el año para consultar que autores estan vivos: \n");
        var anioBuscado = entrada.nextInt();

        List<Autores> autoresVivos = autoresRepositorio
                .findByFechaNacimientoLessThanOrFechaFallecimientoGreaterThanEqual(anioBuscado, anioBuscado);

        Set<String> autoresVivosXanio = autoresVivos.stream()
                .filter(autor -> autor.getFechaNacimiento() != null &&
                        autor.getFechaFallecimiento() != null &&
                        autor.getFechaNacimiento() <= anioBuscado &&
                        autor.getFechaFallecimiento() >= anioBuscado)
                .map(Autores::getNombre)
                .collect(Collectors.toSet());

        if (autoresVivosXanio.isEmpty()) {
            System.out.println("No se encontraron autores que estuvieran vivos en el año " + anioBuscado + ".");
        } else {
            System.out.println("Los autores que estaban vivos en el año " + anioBuscado + " son: \n");
            autoresVivosXanio.forEach(nombre -> System.out.println("Autor: " + nombre));
        }

    }

    private void listarLibrosXIdioma() {
        System.out.println("""
                Ingresa las siglas del idioma con el que quieras filtrar los libros
                Ejemplo:
                en = a libros en Ingles
                es = a libros en Español
                """);
        var idioma = entrada.nextLine().toLowerCase();
        List<Libros> librosPorIdioma = librosRepositorio.findByIdiomas(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros resgistrados en la base de datos.");
        } else {
            System.out.println("Los Libros registrados en la base de datos por el idioma: " + "(" + idioma + ")"
                    + " son los siguientes: \n");

            for (Libros libro : librosPorIdioma) {
                System.out.println("Idioma:" + libro.getIdiomas());
                System.out.println("Título: " + libro.getTitulo());

                Autores autor = libro.getAutor();
                if (autor != null) {
                    System.out.println("Autor: " + autor.getNombre());
                    System.out.println("-------------");
                }

                else {
                    System.out.println("No se encontraron libros con el idioma indicado " + idioma);
                }
            }
        }
    }

}
