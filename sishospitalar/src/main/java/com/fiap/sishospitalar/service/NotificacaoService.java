package com.fiap.sishospitalar.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fiap.sishospitalar.config.RabbitConfig;
import com.fiap.sishospitalar.model.Consulta;

@Service
public class NotificacaoService {

	@RabbitListener(queues = RabbitConfig.NOVA_CONSULTA)
	public void novaConsulta(Consulta consulta) {
		System.out.println("Nova notificação de consulta para o paciente: " + consulta.getPaciente().getNome());
		System.out.println("Consulta marcada");
	}

	@RabbitListener(queues = RabbitConfig.ATUALIZAR_CONSULTA)
	public void atualizarConsulta(Consulta consulta) {
		System.out.println("Nova notificação de consulta para o paciente: " + consulta.getPaciente().getNome());
		System.out.println("Consulta atualizada");
	}
}
