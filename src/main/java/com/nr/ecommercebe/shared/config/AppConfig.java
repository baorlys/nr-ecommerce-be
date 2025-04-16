package com.nr.ecommercebe.shared.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    Dotenv dotenv () {
        return Dotenv.load();
    }

}
