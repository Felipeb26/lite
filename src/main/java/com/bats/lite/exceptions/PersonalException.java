package com.bats.lite.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalException {
	private Throwable throwable;
	private HttpStatus status;
	private String timestamp;
	private String message;

}
