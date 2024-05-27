package com.auth_api.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.auth_api.domain.user.MessageDTO;

@Service
public class NotificacaoService {
	
	private RabbitTemplate rabbitTemplate;
	
	public NotificacaoService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void notificar(MessageDTO message, String exchange) {
		rabbitTemplate.convertAndSend(exchange, "", message);
	}
	
	
	
}
