package com.bats.lite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;

	public String sendMail(String emailFor, String emailSubject) {
		try {

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

			String username = emailFor.substring(0, emailFor.lastIndexOf("@"));

			String message = String.format("<p><b>Bem Vindo</b> %s</p>", username);
			message += String.format("<p><b>%s</b> veja mais</a>", "Caso queira:");

			helper.setFrom("felipeb2silva@gmail.com", "Felipe Batista da Silva");
			helper.setTo(emailFor);
			helper.setSubject(emailSubject);
			helper.setText(message, true);

			mailSender.send(mimeMessage);

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

	public static void main(String[] args) throws Exception {
		URL url = new URL(
			"http://127.0.0.1" + ":" + 8090 + "/SendSMS?username=felipe&password=2001&phone=11971404157&mensagem=teste"
		);
		URLConnection connection = url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		reader.close();
	}
}
