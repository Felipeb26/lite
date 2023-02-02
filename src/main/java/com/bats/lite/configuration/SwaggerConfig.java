package com.bats.lite.configuration;

import com.bats.lite.exceptions.BatsException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("v1")
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select().apis(RequestHandlerSelectors.basePackage("com.bats.lite"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "BatWorks Lite",
                "Teste de applicação com SQLite", "1.0.0",
                "Terms and Service", new Contact("Felipe batista da silva",
                "https://felipeb26.github.io/front_a3/", "felipeb2silva@gmail.com"),
                null, null, new ArrayList<>()
        );
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    private SecurityContext securityContext() {
            return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
            AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
            authorizationScopes[0] = authorizationScope;
            return Arrays.asList(new SecurityReference("Bearer", authorizationScopes));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
