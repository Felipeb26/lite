package com.bats.lite.configuration.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bats.lite.entity.Login;
import com.bats.lite.exceptions.BatsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security-secret}")
    private String secret;

    public String generateToken(Login login) {
        try {
            var algorith = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("API_FOR_DASBHOARD")
                    .withSubject(login.getEmail())
                    .withExpiresAt(expireDate())
                    .sign(algorith);

        } catch (Exception e) {
            throw new BatsException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public String getSubjec(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API_FOR_DASBHOARD")
                    .build()
                    .verify(token).getSubject();
        } catch (TokenExpiredException e) {
            throw new BatsException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private Instant expireDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
