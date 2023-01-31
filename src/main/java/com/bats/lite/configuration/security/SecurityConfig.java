//package com.bats.lite.configuration.security;
//
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	@Bean
//	@Order(SecurityProperties.BASIC_AUTH_ORDER)
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//		http
//			.authorizeHttpRequests()
//			.anyRequest()
//			.authenticated()
//			.and()
//			.httpBasic();
//
//		return http.build();
//	}
//
//}
