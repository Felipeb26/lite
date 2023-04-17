package com.bats.lite.service;

import com.bats.lite.entity.QeueEntity;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.repository.QeueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QeueService {

	private final QeueRepository repository;
	private final EmailService service;

	public String saveLog(String email) {
		try {
			var isValid = service.validEmail(email);
			final String message;
			if (isValid) {
				var warn = service.sendMail(email, "Primeiro Cadastro!");
				if (warn.endsWith("sucesso")) {
					message = String.format("%s email enviado com sucesso", email);
				} else {
					message = warn;
				}
			} else {
				message = String.format("%s email não é valido", email);
			}

			QeueEntity qeue = QeueEntity.builder().valid(isValid).message(message).build();
			repository.save(qeue);
			return message;
		} catch (Exception e) {
			log.error("\nUM ERRO ACONTECEU: {}\n",e.getMessage());
			throw new BatsException(HttpStatus.INTERNAL_SERVER_ERROR, "erro");
		}
	}

}
