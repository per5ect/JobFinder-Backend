package com.jobfinder.backend.jobfinderbackend.config;

import com.jobfinder.backend.jobfinderbackend.models.Role;
import com.jobfinder.backend.jobfinderbackend.models.RoleEnum;
import com.jobfinder.backend.jobfinderbackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Arrays;

@Configuration
public class RoleSeederConfiguration {
    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            Arrays.stream(RoleEnum.values()).forEach(roleEnum -> {
                if (roleRepository.findByRoleName(roleEnum).isEmpty()) {
                    roleRepository.save(new Role(roleEnum));
                }
            });
        };
    }
}
