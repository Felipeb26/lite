package com.bats.lite.configuration;

import com.bats.lite.entity.Login;
import com.bats.lite.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.nonNull;

@Service
public class InitialConfig {

    @Autowired
    private LoginRepository loginRepository;

    @Async
    @Transactional
    public void create_user() {
        var logins = loginRepository.findAll();
        if(!nonNull(logins) ||logins.isEmpty())
            saveDefaultUser();
    }

    @Transactional
    private void saveDefaultUser() {
        var login = Login.builder()
                .email("user@mail.com")
                .senha("2626")
                .build();
        var l = loginRepository.save(login);
        System.out.println(l);
    }

}
