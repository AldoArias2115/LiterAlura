package com.alura.libreria;

import com.alura.libreria.principal.Principal;
import com.alura.libreria.repository.IAutoresRepository;
import com.alura.libreria.repository.ILibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibreriaApplication implements CommandLineRunner {

	@Autowired
	private ILibrosRepository librosRepository;
	@Autowired
	private IAutoresRepository autoresRepository;

	public static void main(String[] args) {
		SpringApplication.run(LibreriaApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(librosRepository, autoresRepository);
		principal.mostrarMenu();
	}
}
