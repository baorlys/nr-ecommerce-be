package com.nr.ecommercebe.module.user.initializer;

import com.nr.ecommercebe.module.user.model.User;
import com.nr.ecommercebe.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleCache roleCache;

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.first-name}")
    private String adminFirstName;
    @Value("${admin.last-name}")
    private String adminLastName;

    @Override
    public void run(String... args) {
        if (userRepository.findUserByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .firstName(adminFirstName)
                    .lastName(adminLastName)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .role(roleCache.getAdminRole())
                    .build();
            userRepository.save(admin);
            log.info("Admin user created");
        } else {
            log.info("Admin user already exists");
        }
    }
}
