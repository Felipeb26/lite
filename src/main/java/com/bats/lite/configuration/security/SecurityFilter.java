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

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final LoginRepository loginRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var auth = recoverToken(request);
        if (auth != null && !auth.isEmpty()) {
            var subject = tokenService.getSubjec(auth);
            var login = loginRepository.findByUsername(subject);

            var authentication = new UsernamePasswordAuthenticationToken(login, null, login.get().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
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
