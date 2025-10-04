package com.fiap.sishospitalar.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.sishospitalar.config.RabbitConfig;
import com.fiap.sishospitalar.exceptions.ConsultaNaoEncontradaException;
import com.fiap.sishospitalar.model.Consulta;
import com.fiap.sishospitalar.repository.ConsultaRepository;

import java.util.List;

@Service
@Transactional
public class AgendamentoService {

	private final ConsultaRepository consultaRepository;
	private final RabbitTemplate rabbitTemplate;

	public AgendamentoService(ConsultaRepository consultaRepository, RabbitTemplate rabbitTemplate) {
		this.consultaRepository = consultaRepository;
		this.rabbitTemplate = rabbitTemplate;
	}

	public Consulta criarConsulta(Consulta consulta) {
		Consulta saved = consultaRepository.save(consulta);
		rabbitTemplate.convertAndSend(RabbitConfig.CONSULTA_EXCHANGE, RabbitConfig.NOVA_CONSULTA, saved);
		return saved;
	}

	public Consulta atualizarConsulta(Consulta consulta) {
		Consulta updated = consultaRepository.save(consulta);
		rabbitTemplate.convertAndSend(RabbitConfig.CONSULTA_EXCHANGE, RabbitConfig.ATUALIZAR_CONSULTA, updated);
		return updated;
	}

	public List<Consulta> listarConsultasPorPaciente(Long pacienteId) {
		return consultaRepository.findByPacienteId(pacienteId);
	}

	public List<Consulta> listarConsultasFuturas() {
		return consultaRepository.findByDataHoraAfter(java.time.LocalDateTime.now());
	}

	public Consulta buscarPorId(Long id) {
		return consultaRepository.findById(id).orElseThrow(() -> new ConsultaNaoEncontradaException("Consulta não encontrada com ID: " + id));
	}
}
