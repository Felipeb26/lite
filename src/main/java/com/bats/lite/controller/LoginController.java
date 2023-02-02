package com.bats.lite.controller;

import com.bats.lite.aop.files.FileGenerate;
import com.bats.lite.aop.files.FileType;
import com.bats.lite.aop.track.TrackTime;
import com.bats.lite.configuration.InitialConfig;
import com.bats.lite.configuration.security.service.TokenService;
import com.bats.lite.dto.TokenDTO;
import com.bats.lite.entity.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Api("Controller de Login")
public class LoginController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private InitialConfig initialConfig;

    @TrackTime
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

        return ResponseEntity.ok().body(object);
    }

}
