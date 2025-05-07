package com.nr.ecommercebe.shared.config;

import com.nr.ecommercebe.module.user.application.domain.RoleName;
import com.nr.ecommercebe.shared.constants.PrivateApi;
import com.nr.ecommercebe.shared.constants.PublicApi;
import com.nr.ecommercebe.shared.exception.CustomAuthenticationEntryPoint;
import com.nr.ecommercebe.module.user.application.service.authentication.CustomUserDetailsService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig  {
    JwtAuthFilter jwtAuthFilter;
    CustomUserDetailsService userDetailsService;
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint(authenticationEntryPoint)
                )
                .authorizeHttpRequests(auth -> auth
                        // API Docs
                        .requestMatchers(
                                PublicApi.API_DOCS,
                                PublicApi.API_DOCS_YAML_FILE,
                                PublicApi.API_DOCS_UI,
                                PublicApi.API_DOCS_UI_HTML
                        ).permitAll()

                        // API
                        .requestMatchers(PrivateApi.API_ADMIN).hasRole(RoleName.ADMIN.name())
                        .requestMatchers(PrivateApi.API_MEDIA).authenticated()
                        .requestMatchers(PrivateApi.API_CURRENT_USER).authenticated()
                        .requestMatchers(PublicApi.OTHER_API).permitAll()

                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(@Value("${app.frontend.url}") String appFrontendUrl) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(appFrontendUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
