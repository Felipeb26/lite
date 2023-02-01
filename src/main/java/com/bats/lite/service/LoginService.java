package com.bats.lite.service;

import com.bats.lite.entity.Login;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public Login findByUsername(String username) {
        Login login = loginRepository.findByUsername(username)
                .orElseThrow(() -> new BatsException(NOT_FOUND, "Usuario não encontrado!"));

        return Login.builder()
                .email(login.getEmail())
                .senha(new BCryptPasswordEncoder().encode(login.getSenha()))
                .build();

    }
}
