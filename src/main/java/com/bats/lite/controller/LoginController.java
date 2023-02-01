package com.bats.lite.controller;

import com.bats.lite.configuration.security.service.TokenService;
import com.bats.lite.entity.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
@Api("Controller de Login")
public class LoginController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    @ApiOperation("Realiza o login e traz o token")
    public ResponseEntity response(@RequestParam String username, @RequestParam String senha) {
        var token = new UsernamePasswordAuthenticationToken(username, senha);
        var auth = manager.authenticate(token);

        Map<String, Object> map = new HashMap<>();

        map.put("token", tokenService.generateToken((Login) auth.getPrincipal()));
        map.put("type", "Bearer");
        map.put("Time", "2hrs");

        return ResponseEntity.ok().body(map);
    }
}
