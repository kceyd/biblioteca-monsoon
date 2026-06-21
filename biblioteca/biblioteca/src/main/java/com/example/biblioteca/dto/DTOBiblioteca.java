package com.example.biblioteca.dto;

import java.time.LocalDate;
import org.springframework.hateoas.RepresentationModel; // 👈 Asegúrate de incluir este import
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false) // Evita problemas de herencia con Lombok
public class DTOBiblioteca extends RepresentationModel<DTOBiblioteca> { // 👈 DEBE EXTENDER ESTA CLASE
    private Long id;
    private Long usuarioId;
    private LocalDate fechaAdquisicion;
    private Double horasJugadas;
    private DTOjuego juego; 
}