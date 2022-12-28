package com.bats.lite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;

	public void sendFullMail(String emailFor, String emailBody, String emailSubject) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("felipeb2silva@gmail.com");
		message.setReplyTo("felipeb2silva@gmail.com");
		message.setTo(emailFor);
		message.setText(emailBody);
		message.setSubject(emailSubject);
		mailSender.send(message);
		System.out.println("email enviado com sucesso");
	}

	public String sendEmailSubject(String email, String subject) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("felipeb2silva@gmail.com");
			message.setReplyTo("felipeb2silva@gmail.com");
			message.setTo(email);
			message.setSubject(subject);
			mailSender.send(message);
			return "email enviado com sucesso";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public boolean validEmail(String email) {
		try {
			InternetAddress address = new InternetAddress(email);
			address.validate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
