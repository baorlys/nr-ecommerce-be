package com.nr.ecommercebe.shared.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VFood E-commerce API")
                        .description("API documentation for the VFood Spring Boot backend")
                        .version("v1.0.0")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("VFood GitHub Documentation")
                        .url("https://github.com/baorlys/nr-ecommerce-be"));
    }
}