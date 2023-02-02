package com.bats.lite.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@ControllerAdvice
public class BatsExceptionHandler {

    @ExceptionHandler(BatsException.class)
    public ResponseEntity<?> entity(BatsException e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        log.error(e.getReason());
        log.info(e.getMessage());
        log.warn(e.getMessage());

        PersonalException exception = PersonalException.builder()
                .message(e.getReason())
                .status(e.getStatus())
                .timestamp(dateTime.format(formatter))
                .throwable(e.getCause())
                .build();
        return ResponseEntity.status(e.getStatus()).body(exception);
    }
}
