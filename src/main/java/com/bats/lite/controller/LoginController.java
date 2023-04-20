package com.bats.lite.controller;

import com.bats.lite.configuration.InitialConfig;
import com.bats.lite.configuration.security.service.TokenService;
import com.bats.lite.dto.TokenDTO;
import com.bats.lite.entity.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
@Api("Controller de Login")
@CacheConfig(cacheNames = "login")
public class LoginController {

    private static int requests = 0;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private InitialConfig initialConfig;

    @Cacheable
    @GetMapping
    @ApiOperation("Realiza o login e traz o token")
    public ResponseEntity<Object> response(@RequestParam String username, @RequestParam String senha) {
        initialConfig.create_user();

        var token = new UsernamePasswordAuthenticationToken(username, senha);
        var auth = manager.authenticate(token);
        var object = TokenDTO.builder()
                .token(tokenService.generateToken((Login) auth.getPrincipal()))
                .type("Bearer")
                .time("2 hrs").build();

        System.out.printf("\n\nREQUETS made: %s \n\n", requests);
        requests++;
        return ResponseEntity.ok().body(object);
    }

}
