package com.bats.lite.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BatsException extends ResponseStatusException {

	public BatsException(HttpStatus status, String reason) {
		super(status, reason);
	}

	public BatsException(HttpStatus status, String reason, Throwable cause) {
		super(status, reason, cause);
	}
}