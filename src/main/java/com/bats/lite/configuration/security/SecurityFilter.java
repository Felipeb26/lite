package com.bats.lite.configuration.security;

import com.bats.lite.configuration.security.service.TokenService;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.*;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final LoginRepository loginRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  {
        try {
            var auth = recoverToken(request);
            if (auth != null && !auth.isEmpty()) {
                var subject = tokenService.getSubjec(auth);
                var login = loginRepository.findByUsername(subject).orElseThrow(() -> new BatsException(NOT_FOUND, "User dont was found"));

                var authentication = new UsernamePasswordAuthenticationToken(login, null, login.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (BatsException e) {
            throw new BatsException(e.getStatus(), e.getReason());
        } catch (ServletException | IOException e) {
            throw new BatsException(INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (nonNull(token)) {
            if (!token.startsWith("Bearer")) {
                throw new BatsException(HttpStatus.BAD_REQUEST, "Token invalido ou expirado");
            }
            return token.replace("Bearer", "").trim();
        } else {
            return null;
        }
    }
}
