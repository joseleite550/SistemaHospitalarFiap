package com.fiap.sishospitalar.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fiap.sishospitalar.config.RabbitConfig;
import com.fiap.sishospitalar.model.Consulta;
import com.fiap.sishospitalar.repository.ConsultaRepository;

@Service
public class NotificacaoService {

	private final ConsultaRepository consultaRepository;

	
    public NotificacaoService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }
    
	@RabbitListener(queues = RabbitConfig.NOVA_CONSULTA)
	public void novaConsulta(Consulta consulta) {
		enviarEmail(consulta,"Consulta marcada");
	}

	@RabbitListener(queues = RabbitConfig.ATUALIZAR_CONSULTA)
	public void atualizarConsulta(Consulta consulta) {
		enviarEmail(consulta,"Consulta atualizada");
	}

	@Scheduled(fixedRate = 30000)
	public void enviarLembretesDiarios() {
		LocalDateTime agora = LocalDateTime.now();
		LocalDateTime daqui1Dia = agora.plusDays(1);

		List<Consulta> futuras = consultaRepository.findByDataHoraAfter(agora);
		futuras.stream().filter(c -> !c.getDataHora().isBefore(agora) && c.getDataHora().isBefore(daqui1Dia))
				.forEach(c -> enviarEmail(c, "Lembrete de consulta amanhã"));
	}

	private void enviarEmail(Consulta consulta, String assunto) {
		System.out.println("Nova notificação de consulta para o paciente: " + consulta.getPaciente().getNome());
		System.out.println(assunto);
		System.out.println("Email enviado para: " + consulta.getPaciente().getEmail());
	}
}
