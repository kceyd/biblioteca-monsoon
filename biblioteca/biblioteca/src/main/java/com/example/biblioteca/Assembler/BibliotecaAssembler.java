package com.example.biblioteca.Assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.biblioteca.controller.controlbiblioteca;
import com.example.biblioteca.dto.DTOBiblioteca;

@Component
public class BibliotecaAssembler extends RepresentationModelAssemblerSupport<DTOBiblioteca, DTOBiblioteca> {

    public BibliotecaAssembler() {
        super(controlbiblioteca.class, DTOBiblioteca.class);
    }

    @SuppressWarnings("null")
    @Override
    public DTOBiblioteca toModel(@NonNull DTOBiblioteca dto) {
        // Vincula el endpoint individual del controlador
        dto.add(linkTo(methodOn(controlbiblioteca.class).obtenerEntrada(dto.getId())).withSelfRel());
        // Vincula el endpoint de la lista completa del controlador
        dto.add(linkTo(methodOn(controlbiblioteca.class).obtenerEntradas()).withRel("lista-completa"));
        return dto;
    }
}