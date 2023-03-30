package com.bats.lite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableFeignClients
@SpringBootApplication
public class LiteApplication {

  public static void main(String[] args) {
    SpringApplication.run(LiteApplication.class, args);
  }
}
