package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.CompanyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.dto.CreateVacancyDTO;
import com.jobfinder.backend.jobfinderbackend.models.Company;
import com.jobfinder.backend.jobfinderbackend.services.CompanyService;
import com.jobfinder.backend.jobfinderbackend.services.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/company")
@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final VacancyService vacancyService;

    @GetMapping("/my-company")
    public ResponseEntity<CompanyDetailsDTO> getCurrentCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем email из токена
        System.out.println("🔍 Полученный email из токена: " + email); // ЛОГ

        CompanyDetailsDTO companyDetails = companyService.getCompanyDetails(email);
        return ResponseEntity.ok(companyDetails);
    }


    @PostMapping("/create-vacancy")
    public ResponseEntity<?> createVacancy(
            @RequestBody CreateVacancyDTO createVacancyDTO,
            @AuthenticationPrincipal Company company
            ){
        vacancyService.createVacancy(createVacancyDTO, company);
        return ResponseEntity.ok("Vacancy created");
    }

}
