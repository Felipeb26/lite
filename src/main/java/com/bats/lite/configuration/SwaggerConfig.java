package com.bats.lite.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(meta());
    }

    private Info meta() {
        return new Info().title("API CARMESIM").version("1.0.0")
                .contact(new Contact().name("Felipe Batista da Silva").email("felipeb2silva@gmail.com")
                        .url("https://www.linkedin.com/in/felipebatista-silva/"))
                .description("API desenvolvida para ser utilizada em conjunto com o site Space Carmesim")
                .termsOfService(null).license(license());
    }

    private License license() {
        License license = new License();
        license.setName("APACHE 2.0");
        license.setUrl("https://www.apache.org/licenses/LICENSE-2.0");

        return license;
    }

}
