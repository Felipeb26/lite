package com.bats.lite.service;

import com.bats.lite.entity.Login;

public interface LoginService {

    Login findByUsername(String username);
}
