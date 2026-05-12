package com.example.biblioteca.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.biblioteca.dto.DTOjuego;
import com.example.biblioteca.model.bibliotecaus;
import com.example.biblioteca.service.serviciobiblioteca;

@RestController
@RequestMapping("/api/v0/biblioteca")
public class controlbiblioteca {
    @Autowired
    private serviciobiblioteca serviciobibliotecaUsuario;

    @GetMapping
    public List<bibliotecaus> obtenerEntradas() {
        return serviciobibliotecaUsuario.obtenerEntradas();
    }

    @PostMapping
    public void crearEntrada(@RequestBody bibliotecaus entrada) {
        serviciobibliotecaUsuario.crearEntrada(entrada);
    }

    @GetMapping("/{id}")
    public bibliotecaus obtenerEntrada(@PathVariable Long id) {
        return serviciobibliotecaUsuario.obtenerEntrada(id);
    }

    @PutMapping("/{id}")
    public void actualizarEntrada(@PathVariable Long id, @RequestBody bibliotecaus entrada) {
        entrada.setId(id);
        serviciobibliotecaUsuario.actualizarEntrada(entrada);
    }

    @DeleteMapping("/{id}")
    public void eliminarEntrada(@PathVariable Long id) {
        serviciobibliotecaUsuario.eliminarEntrada(id);
    }

    @GetMapping("/juegos")
    public List<DTOjuego> obtenerTodosLosJuegos() {
        return serviciobibliotecaUsuario.obtenerTodosLosJuegos();
    }

    @GetMapping("/juegos/{juegoId}")
    public DTOjuego obtenerJuego(@PathVariable Long juegoId) {
        return serviciobibliotecaUsuario.obtenerJuego(juegoId);  
    }
}


