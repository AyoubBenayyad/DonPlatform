package com.plateforme.dons.config;

import com.plateforme.dons.entity.Role;
import com.plateforme.dons.entity.User;
import com.plateforme.dons.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("ayoub").isEmpty()) {
            User ayoub = User.builder()
                    .username("ayoub")
                    .email("ayoub@example.com")
                    .password(passwordEncoder.encode("ayoub"))
                    .role(Role.USER)
                    .enabled(true)
                    .build();
            userRepository.save(ayoub);
            System.out.println("Default user 'ayoub' created successfully.");
        }

        if (userRepository.findByUsername("bakr").isEmpty()) {
            User bakr = User.builder()
                    .username("bakr")
                    .email("bakr@example.com")
                    .password(passwordEncoder.encode("bakr"))
                    .role(Role.USER)
                    .enabled(true)
                    .build();
            userRepository.save(bakr);
            System.out.println("Default user 'bakr' created successfully.");
        }
    }
}
