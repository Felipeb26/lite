package com.bats.lite.amqp;

import com.bats.lite.entity.User;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.service.QeueService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AMQPListener {

	@Autowired
	private QeueService qeueService;

	@RabbitListener(queues = "usuario.cadastrado")
	public void receveMessage(User user) {
		try {
			qeueService.saveLog(user.getEmail());
		}catch (Exception e){
			throw new BatsException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}
}