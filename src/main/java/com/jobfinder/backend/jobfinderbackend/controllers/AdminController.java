package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.CreateTechnologyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.TechnologyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.UserDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.services.TechnologyService;
import com.jobfinder.backend.jobfinderbackend.services.UserService;
import com.jobfinder.backend.jobfinderbackend.services.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TechnologyService technologyService;
    private final UserService userService;
    private final VacancyService vacancyService;


    @GetMapping("/my-account")
    public ResponseEntity<UserDetailsDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем email из токена

        UserDetailsDTO userDetails = userService.getUserDetails(email);
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/add-technology")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addTechnology(@RequestBody CreateTechnologyDTO input){
        technologyService.createTechnology(input);
        return ResponseEntity.ok("Successfully added technology");
    }

    @DeleteMapping("/delete-technology/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteTechnologyById(@PathVariable Long id){
        technologyService.deleteTechnologyById(id);
        return ResponseEntity.ok("Successfully deleted technology");
    }

    @DeleteMapping("/delete-vacancy/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteVacancyById(@PathVariable Long id){
        vacancyService.deleteVacancyById(id);
        return ResponseEntity.ok("Successfully deleted technology");
    }
}
