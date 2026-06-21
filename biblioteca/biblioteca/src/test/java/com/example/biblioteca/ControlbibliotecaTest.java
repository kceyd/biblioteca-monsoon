package com.example.biblioteca;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.biblioteca.Assembler.BibliotecaAssembler;
import com.example.biblioteca.controller.controlbiblioteca;
import com.example.biblioteca.dto.DTOBiblioteca;
import com.example.biblioteca.dto.DTOjuego;
import com.example.biblioteca.model.bibliotecaus;
import com.example.biblioteca.service.serviciobiblioteca;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controlbiblioteca.class)
@Import(BibliotecaAssembler.class) // Importante para que Spring reconozca los enlaces de HATEOAS
public class ControlbibliotecaTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private serviciobiblioteca servicio;

    @Autowired
    private ObjectMapper objectMapper;

    private DTOBiblioteca dtoMuestra;
    private bibliotecaus entidadMuestra;

    @BeforeEach
    void setUp() {
        // Creamos el juego sin usar .setNombre() para evitar el error de compilación
        DTOjuego juegoDto = new DTOjuego();
        juegoDto.setId(100L);

        // Instanciamos el DTO de respuesta con datos simulados
        dtoMuestra = new DTOBiblioteca();
        dtoMuestra.setId(1L);
        dtoMuestra.setUsuarioId(5L);
        dtoMuestra.setFechaAdquisicion(LocalDate.now());
        dtoMuestra.setHorasJugadas(10.0);
        dtoMuestra.setJuego(juegoDto);

        // Instanciamos la entidad de entrada para las pruebas del método POST
        entidadMuestra = new bibliotecaus();
        entidadMuestra.setId(1L);
        entidadMuestra.setUsuarioId(5L);
        entidadMuestra.setJuegoId(100L);
        entidadMuestra.setHorasJugadas(10.0);
    }

    @SuppressWarnings("null")
    @Test
    void testObtenerEntradaPorId_DebeRetornarDTOMasHateoas() throws Exception {
        // Configuramos el mock para que devuelva nuestro DTO simulado
        when(servicio.obtenerEntradaConJuego(1L)).thenReturn(dtoMuestra);

        // Simulamos la petición GET y verificamos los datos y la estructura HATEOAS (_links)
        mockMvc.perform(get("/api/v0/biblioteca/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value(5))
                .andExpect(jsonPath("$.horasJugadas").value(10.0))
                .andExpect(jsonPath("$._links.self.href").exists()) 
                .andExpect(jsonPath("$._links.lista-completa.href").exists());
    }

    @SuppressWarnings("null")
    @Test
    void testCrearEntrada_DebeRetornarStatusOk() throws Exception {
        // Simulamos la petición POST convirtiendo la entidad a JSON
        mockMvc.perform(post("/api/v0/biblioteca")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entidadMuestra)))
                .andExpect(status().isOk());
    }
}