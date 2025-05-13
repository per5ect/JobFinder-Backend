package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.CompanyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.dto.CreateVacancyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.ResponsesToVacancyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VacancyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.models.ApplicationStatusEnum;
import com.jobfinder.backend.jobfinderbackend.models.Company;
import com.jobfinder.backend.jobfinderbackend.services.CompanyService;
import com.jobfinder.backend.jobfinderbackend.services.VacancyApplicationsService;
import com.jobfinder.backend.jobfinderbackend.services.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/company")
@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final VacancyService vacancyService;
    private final VacancyApplicationsService vacancyApplicationsService;

    @GetMapping("/my-company")
    public ResponseEntity<CompanyDetailsDTO> getCurrentCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println("Email from token: " + email);

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

    @GetMapping("/my-vacancies")
    public ResponseEntity<List<VacancyDetailsDTO>> getMyCompanyVacancies(@AuthenticationPrincipal Company company) {
        List<VacancyDetailsDTO> result = companyService.getCompanyVacancies(company.getUsername());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/my-vacancies/{vacancyId}/applications")
    public ResponseEntity<List<ResponsesToVacancyDTO>> getVacanciesForCompany(
            @AuthenticationPrincipal Company company,
            @PathVariable Long vacancyId
    ) {
        List<ResponsesToVacancyDTO> applications = vacancyApplicationsService.getApplicationsByVacancyId(vacancyId, company.getUsername());
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/applications/{applicationId}/accept")
    public ResponseEntity<String> acceptApplication(
            @AuthenticationPrincipal Company company,
            @PathVariable Long applicationId
    ){
        vacancyApplicationsService.updateApplicationStatus(applicationId, company.getUsername(), ApplicationStatusEnum.ACCEPTED);
        return ResponseEntity.ok("Application accepted successfully");
    }

    @PatchMapping("/applications/{applicationId}/reject")
    public ResponseEntity<String> rejectApplication(
            @AuthenticationPrincipal Company company,
            @PathVariable Long applicationId
    ) {
        vacancyApplicationsService.updateApplicationStatus(applicationId, company.getUsername(), ApplicationStatusEnum.REJECTED);
        return ResponseEntity.ok("Application rejected successfully");
    }

}
