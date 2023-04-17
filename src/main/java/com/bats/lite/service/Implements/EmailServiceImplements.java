package com.bats.lite.service.Implements;

import com.bats.lite.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class EmailServiceImplements implements EmailService {

    private final JavaMailSender mailSender;
    private static AtomicInteger integer = new AtomicInteger(0);

    public String sendMail(String emailFor, String emailSubject) {
        try {
            if (integer.get() < 80) {
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
            } else {
                return "email não pode ser enviado agora";
            }
            integer.getAndIncrement();
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
