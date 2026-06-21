package com.example.biblioteca.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.biblioteca.dto.DTOBiblioteca;
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

    public void crearEntrada(@NonNull bibliotecaus entrada) {        
        bibliotecaUsuarioRepository.save(entrada);
    }

    public void actualizarEntrada(@NonNull bibliotecaus entrada) {
        bibliotecaUsuarioRepository.save(entrada);
    }

    public void eliminarEntrada(@NonNull Long id) {
        bibliotecaUsuarioRepository.deleteById(id);
    }

    
    public DTOBiblioteca obtenerEntradaConJuego(@NonNull Long id) {
        bibliotecaus entrada = bibliotecaUsuarioRepository.findById(id).orElse(null);
        if (entrada == null) return null;

        DTOjuego juegoDto = null;
        try {
            juegoDto = obtenerJuego(entrada.getJuegoId());
        } catch (Exception e) {
            // Fallback por si el microservicio de juegos está apagado
            juegoDto = new DTOjuego();
            juegoDto.setId(entrada.getJuegoId());
        }

        
        DTOBiblioteca dto = new DTOBiblioteca();
        dto.setId(entrada.getId());
        dto.setUsuarioId(entrada.getUsuarioId());
        dto.setFechaAdquisicion(entrada.getFechaAdquisicion());
        dto.setHorasJugadas(entrada.getHorasJugadas());
        dto.setJuego(juegoDto);

        return dto;
    }

    
    public List<DTOBiblioteca> obtenerEntradasConJuegos() {
        List<bibliotecaus> entradas = bibliotecaUsuarioRepository.findAll();
        
        return entradas.stream().map(entrada -> {
            DTOjuego juegoDto = null;
            try {
                juegoDto = obtenerJuego(entrada.getJuegoId());
            } catch (Exception e) {
                juegoDto = new DTOjuego();
                juegoDto.setId(entrada.getJuegoId());
            }

            DTOBiblioteca dto = new DTOBiblioteca();
            dto.setId(entrada.getId());
            dto.setUsuarioId(entrada.getUsuarioId());
            dto.setFechaAdquisicion(entrada.getFechaAdquisicion());
            dto.setHorasJugadas(entrada.getHorasJugadas());
            dto.setJuego(juegoDto);
            return dto;
        }).collect(Collectors.toList());
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