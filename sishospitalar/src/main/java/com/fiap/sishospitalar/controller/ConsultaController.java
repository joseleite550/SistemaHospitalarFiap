package com.fiap.sishospitalar.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.fiap.sishospitalar.model.Consulta;
import com.fiap.sishospitalar.model.Usuario;
import com.fiap.sishospitalar.repository.UsuarioRepository;
import com.fiap.sishospitalar.service.AgendamentoService;

@Controller
public class ConsultaController {

	private final AgendamentoService agendamentoService;
	private final UsuarioRepository usuarioRepository;

	public ConsultaController(AgendamentoService agendamentoService, UsuarioRepository usuarioRepository) {
		this.agendamentoService = agendamentoService;
		this.usuarioRepository = usuarioRepository;
	}

	@PreAuthorize("hasRole('PACIENTE') or hasRole('MEDICO') or hasRole('ENFERMEIRO')")
	@QueryMapping
	public List<Consulta> consultasPorPaciente(@Argument Long pacienteId) {
		return agendamentoService.listarConsultasPorPaciente(pacienteId);
	}

	@PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
	@QueryMapping
	public List<Consulta> consultasFuturas() {
		return agendamentoService.listarConsultasFuturas();
	}

	@PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
	@MutationMapping
	public Consulta criarConsulta(@Argument Long pacienteId, @Argument Long medicoId, @Argument String dataHora,
			@Argument String observacoes) {

		Usuario paciente = usuarioRepository.findById(pacienteId)
				.orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

		Usuario medico = usuarioRepository.findById(medicoId)
				.orElseThrow(() -> new RuntimeException("Médico não encontrado"));

		Consulta consulta = new Consulta();
		consulta.setPaciente(paciente);
		consulta.setMedico(medico);
		consulta.setDataHora(LocalDateTime.parse(dataHora));
		consulta.setObservacoes(observacoes);

		return agendamentoService.criarConsulta(consulta);
	}

	@PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
	@MutationMapping
	public Consulta atualizarConsulta(@Argument Long consultaId, @Argument String observacoes) {

		Consulta consulta = agendamentoService.buscarPorId(consultaId);
		consulta.setObservacoes(observacoes);

		return agendamentoService.atualizarConsulta(consulta);
	}
}
