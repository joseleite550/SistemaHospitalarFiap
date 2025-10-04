package com.fiap.sishospitalar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiap.sishospitalar.model.Consulta;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPacienteId(Long pacienteId);
    List<Consulta> findByMedicoId(Long medicoId);
    List<Consulta> findByDataHoraAfter(LocalDateTime dataHora);
}
