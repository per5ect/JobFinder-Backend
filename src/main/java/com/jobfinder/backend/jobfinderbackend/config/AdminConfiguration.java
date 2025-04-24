package com.jobfinder.backend.jobfinderbackend.config;

import com.jobfinder.backend.jobfinderbackend.models.Role;
import com.jobfinder.backend.jobfinderbackend.models.RoleEnum;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.repository.RoleRepository;
import com.jobfinder.backend.jobfinderbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Configuration
@DependsOn("initRoles")
public class AdminConfiguration {
    @Bean
    public CommandLineRunner createAdmin(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            String adminEmail = "admin@admin";

            Optional<User> isAdminHere = userRepository.findByEmail(adminEmail);
            if (isAdminHere.isEmpty()) {
                Role adminRole = roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("role admin not found"));


                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setFirstName("Greatest");
                admin.setLastName("admin");
                admin.setRoles(Set.of(adminRole));
                admin.setEnabled(true);

                userRepository.save(admin);
                System.out.println("Admin created");

            }
        };
    }

}
