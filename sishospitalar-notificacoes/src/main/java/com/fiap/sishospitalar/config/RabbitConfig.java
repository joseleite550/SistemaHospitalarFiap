package com.fiap.sishospitalar.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {

    public static final String NOVA_CONSULTA = "nova-consulta";
    public static final String ATUALIZAR_CONSULTA = "atualizar-consulta";
    public static final String CONSULTA_EXCHANGE = "consulta-exchange";
    public static final String CANCELAR_CONSULTA = "cancelar-consulta";

    @Bean
    TopicExchange consultaExchange() {
        return new TopicExchange(CONSULTA_EXCHANGE);
    }

    @Bean
    Queue filaNovaConsulta() {
        return new Queue(NOVA_CONSULTA, true);
    }

    @Bean
    Queue filaAtualizarConsulta() {
        return new Queue(ATUALIZAR_CONSULTA, true);
    }

    @Bean
    Binding bindingNovaConsulta(@Qualifier("filaNovaConsulta") Queue filaNovaConsulta, TopicExchange consultaExchange) {
        return BindingBuilder.bind(filaNovaConsulta).to(consultaExchange).with(NOVA_CONSULTA);
    }

    @Bean
    Binding bindingAtualizarConsulta(@Qualifier("filaAtualizarConsulta") Queue filaAtualizarConsulta, TopicExchange consultaExchange) {
        return BindingBuilder.bind(filaAtualizarConsulta).to(consultaExchange).with(ATUALIZAR_CONSULTA);
    }
    
	@Bean
	Binding bindingCancelarConsulta(@Qualifier("filaCancelarConsulta") Queue filaCancelarConsulta,
			TopicExchange consultaExchange) {
		return BindingBuilder.bind(filaCancelarConsulta).to(consultaExchange).with(CANCELAR_CONSULTA);
	}

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
