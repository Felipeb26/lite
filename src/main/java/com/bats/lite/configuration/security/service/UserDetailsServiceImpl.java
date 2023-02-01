package com.bats.lite.configuration.security.service;

import com.bats.lite.entity.Login;
import com.bats.lite.exceptions.BatsException;
import com.bats.lite.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.NOT_FOUND;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginService.findByUsername(username);
        return login;
    }
}
