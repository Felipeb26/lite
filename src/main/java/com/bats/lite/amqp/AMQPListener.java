package com.bats.lite.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bats.lite.entity.QeueEntity;
import com.bats.lite.entity.User;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.repository.QeueRepository;
import com.bats.lite.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AMQPListener {

	private final QeueRepository repository;
	private final EmailService service;

	@RabbitListener(queues = "usuario.cadastrado")
	public void receveMessage(User user) {
		try {
			String email = user.getEmail();
			var isValid = service.validEmail(user.getEmail());
			final String message;
			if (isValid) {
				String warn = service.sendMail(email, "Primeiro Cadastro!");
				if (warn.endsWith("sucesso")) {
					message = String.format("%s email enviado com sucesso", email);
				} else {
					message = warn;
				}
			} else {
				throw new BatsException(HttpStatus.INTERNAL_SERVER_ERROR,
						String.format("%s email não é valido", email));
			}

			QeueEntity qeue = QeueEntity.builder().valid(isValid).message(message).build();
			repository.save(qeue);
		} catch (Exception e) {
			log.error("\nUM ERRO ACONTECEU: {}\n", e.getMessage());
			throw new BatsException(HttpStatus.INTERNAL_SERVER_ERROR, "erro");
		}
	}
}