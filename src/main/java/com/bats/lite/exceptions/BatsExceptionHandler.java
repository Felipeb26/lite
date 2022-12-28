package com.bats.lite.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static java.util.Objects.nonNull;
@ControllerAdvice
public class BatsExceptionHandler {

	@ExceptionHandler(value = {BatsException.class})
	public ResponseEntity<?> entity(BatsException e) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

		PersonalException exception = PersonalException.builder()
			.message(e.getReason())
			.status(nonNull(e.getStatus()) ? BAD_REQUEST : e.getStatus())
			.timestamp(dateTime.format(formatter))
			.throwable(e.getCause())
			.build();
		return ResponseEntity.status(BAD_REQUEST).body(exception);
	}
}
