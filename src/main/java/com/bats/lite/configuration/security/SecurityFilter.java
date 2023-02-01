package com.bats.lite.configuration.security;

import com.bats.lite.configuration.security.service.TokenService;
import com.bats.lite.exceptions.BatsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var auth = recoverToken(request);

        var subject = tokenService.getSubjec(auth);

        System.out.println(subject);
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (nonNull(token)) {
            if (!token.startsWith("Bearer")) {
                throw new BatsException(HttpStatus.BAD_REQUEST, "Token invalido ou expirado");
            }
            return token.replace("Bearer", "");
        }
        throw new BatsException(HttpStatus.BAD_REQUEST, "Token não enviado");
    }
}
