package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.CompanyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VacancyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.models.Company;
import com.jobfinder.backend.jobfinderbackend.models.Technology;
import com.jobfinder.backend.jobfinderbackend.models.Vacancy;
import com.jobfinder.backend.jobfinderbackend.repository.CompanyRepository;
import com.jobfinder.backend.jobfinderbackend.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final VacancyRepository vacancyRepository;

    public CompanyDetailsDTO getCompanyDetails(String email) {
        Company company = companyRepository.findByCompanyEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return new CompanyDetailsDTO(
                company.getId(),
                company.getCompanyEmail(),
                company.getCompanyName(),
                company.getCompanyPhone(),
                company.isEnabled()
        );
    }

    public List<VacancyDetailsDTO> getCompanyVacancies(String companyEmail) {
        Company company = companyRepository.findByCompanyEmail(companyEmail)
                .orElseThrow(() -> new IllegalStateException("Company not found"));

        List<Vacancy> vacancies = vacancyRepository.findByCompany(company);

        return vacancies.stream().map(vacancy -> {
            VacancyDetailsDTO dto = new VacancyDetailsDTO();
            dto.setId(vacancy.getId());
            dto.setTitle(vacancy.getTitle());
            dto.setShortDescription(vacancy.getShortDescription());
            dto.setLocation(vacancy.getLocation());
            dto.setKnowledgeLevel(vacancy.getKnowledgeLevel());
            dto.setTypeOfEmployment(vacancy.getTypeOfEmployment());
            dto.setWorkExperience(vacancy.getWorkExperience());
            dto.setWorkMode(vacancy.getWorkMode());
            dto.setSalary(vacancy.getSalary());

            dto.setCompanyName(company.getCompanyName());
            dto.setCompanyEmail(company.getCompanyEmail());
            dto.setCompanyPhone(company.getCompanyPhone());

            dto.setRequirements(vacancy.getRequirements());
            dto.setOfferings(vacancy.getOfferings());

            Set<String> techNames = vacancy.getTechnologies().stream()
                    .map(Technology::getTechnologyName)
                    .collect(Collectors.toSet());

            dto.setTechnologyNames(techNames);

            return dto;
        }).toList();
    }

}
