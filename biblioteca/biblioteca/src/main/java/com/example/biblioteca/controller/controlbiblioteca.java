package com.example.biblioteca.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.example.biblioteca.Assembler.BibliotecaAssembler;
import com.example.biblioteca.dto.DTOBiblioteca;
import com.example.biblioteca.model.bibliotecaus;
import com.example.biblioteca.service.serviciobiblioteca;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v0/biblioteca")
@Tag(name = "Biblioteca API", description = "Endpoints de la biblioteca usando DTOs y Assembler para HATEOAS")
public class controlbiblioteca {

    @Autowired
    private serviciobiblioteca serviciobibliotecaUsuario;

    @Autowired
    private BibliotecaAssembler Assembler;

    @SuppressWarnings("null")
    @GetMapping
    @Operation(summary = "Listar todas las entradas de la biblioteca")
    public CollectionModel<DTOBiblioteca> obtenerEntradas() {
        List<DTOBiblioteca> dtos = serviciobibliotecaUsuario.obtenerEntradasConJuegos();

        // El assembler convierte la lista entera mapeando los enlaces automáticamente
        List<DTOBiblioteca> dtosConHateoas = dtos.stream()
                .map(Assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(dtosConHateoas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una entrada específica por ID")
    public ResponseEntity<DTOBiblioteca> obtenerEntrada(@PathVariable @NonNull Long id) {
        DTOBiblioteca dto = serviciobibliotecaUsuario.obtenerEntradaConJuego(id);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        // El assembler le inyecta sus enlaces hipermedia
        return ResponseEntity.ok(Assembler.toModel(dto));
    }

    @PostMapping
    public ResponseEntity<Void> crearEntrada(@RequestBody @NonNull bibliotecaus entrada) {
        serviciobibliotecaUsuario.crearEntrada(entrada);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarEntrada(@PathVariable Long id, @RequestBody bibliotecaus entrada) {
        entrada.setId(id);
        serviciobibliotecaUsuario.actualizarEntrada(entrada);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntrada(@PathVariable @NonNull Long id) {
        serviciobibliotecaUsuario.eliminarEntrada(id);
        return ResponseEntity.ok().build();
    }
}