package com.bats.lite.service;

public interface EmailService {

	String sendMail(String emailFor, String emailSubject);

	boolean validEmail(String email);

}
