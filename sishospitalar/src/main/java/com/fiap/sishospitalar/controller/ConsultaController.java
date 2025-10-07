package com.fiap.sishospitalar.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import com.fiap.sishospitalar.exceptions.MedicoNaoEncontradoException;
import com.fiap.sishospitalar.exceptions.PacienteNaoEncontradoException;
import com.fiap.sishospitalar.model.Consulta;
import com.fiap.sishospitalar.model.Perfil;
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

	@PreAuthorize("isAuthenticated()")
	@QueryMapping
	public List<Consulta> consultasPorPaciente(@Argument Long pacienteId) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// Obtém o email/username do JWT
		String userEmail = ((User) auth.getPrincipal()).getUsername();

		Usuario usuarioLogado = usuarioRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userEmail));
		
		if (usuarioLogado.getPerfil() == Perfil.PACIENTE) {
			if (pacienteId != null && !pacienteId.equals(usuarioLogado.getId())) {
				throw new SecurityException("Acesso negado: Pacientes só podem ver suas próprias consultas.");
			}
			return agendamentoService.listarConsultasPorPaciente(usuarioLogado.getId());
		}
		
		if (pacienteId == null) {
			throw new IllegalArgumentException("O ID do paciente é obrigatório para Médicos e Enfermeiros.");
		}
		
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
				.orElseThrow(() -> new PacienteNaoEncontradoException("Paciente não encontrado"));

		if(!Perfil.PACIENTE.name().equals(paciente.getPerfil().name())) {
			throw new PacienteNaoEncontradoException("Paciente não encontrado");
		}
		
		Usuario medico = usuarioRepository.findById(medicoId)
				.orElseThrow(() -> new MedicoNaoEncontradoException("Médico não encontrado"));

		if(!Perfil.MEDICO.name().equals(medico.getPerfil().name())) {
			throw new MedicoNaoEncontradoException("Médico não encontrado");
		}
		
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
	
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
	@MutationMapping
	public Long cancelarConsulta(@Argument Long consultaId) {
		agendamentoService.cancelarConsulta(consultaId);
		return consultaId;
	}
}