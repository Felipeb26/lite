package com.bats.lite.configuration.security.service;

import com.bats.lite.configuration.InitialConfig;
import com.bats.lite.repository.LoginRepository;
import com.bats.lite.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginService loginService;
    private final InitialConfig config;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        config.create_user();
        return loginService.findByUsername(username);
    }

}
