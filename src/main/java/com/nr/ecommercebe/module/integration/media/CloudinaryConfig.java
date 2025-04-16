package com.nr.ecommercebe.module.integration.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    Dotenv dotenv;

    public CloudinaryConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    static final String CLOUDINARY_NAME = "CLOUDINARY_NAME";
    static final String CLOUDINARY_API_KEY = "CLOUDINARY_API_KEY";
    static final String CLOUDINARY_API_SECRET = "CLOUDINARY_API_SECRET";

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", dotenv.get(CLOUDINARY_NAME),
                "api_key", dotenv.get(CLOUDINARY_API_KEY),
                "api_secret", dotenv.get(CLOUDINARY_API_SECRET)));
    }
}
