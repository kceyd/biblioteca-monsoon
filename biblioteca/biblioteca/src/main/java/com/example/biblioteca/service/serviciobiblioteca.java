package com.example.biblioteca.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.biblioteca.dto.DTOjuego;
import com.example.biblioteca.model.bibliotecaus;
import com.example.biblioteca.repository.repobiblioteca;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class serviciobiblioteca {
    @Autowired
    private repobiblioteca bibliotecaUsuarioRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8085/api/v0/juegos")
            .build();

    public void crearEntrada(bibliotecaus entrada) {        
        bibliotecaUsuarioRepository.save(entrada);
    }

    public List<bibliotecaus> obtenerEntradas() {
        return bibliotecaUsuarioRepository.findAll();
    }

    public bibliotecaus obtenerEntrada(Long id) {
        return bibliotecaUsuarioRepository.findById(id).orElse(null);
    }

    public void actualizarEntrada(bibliotecaus entrada) {
        bibliotecaUsuarioRepository.save(entrada);
    }

    public void eliminarEntrada(Long id) {
        bibliotecaUsuarioRepository.deleteById(id);
    }

    public DTOjuego obtenerJuego(Long juegoId) {
        return webClient.get()
                .uri("/{id}", juegoId)
                .retrieve()
                .bodyToMono(DTOjuego.class)
                .block();
    }

    public List<DTOjuego> obtenerTodosLosJuegos() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(DTOjuego.class)
                .collectList()
                .block();
    }
}