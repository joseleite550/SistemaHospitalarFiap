package com.fiap.sishospitalar.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultas")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataHora;
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Usuario medico;
    @ManyToOne
    @JoinColumn(name = "enfermeiro_id")
    private Usuario enfermeiro;
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;
    private String observacoes;
}
