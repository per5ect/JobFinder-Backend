package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.CompanyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.models.Company;
import com.jobfinder.backend.jobfinderbackend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;


    public CompanyDetailsDTO getCompanyDetails(String email) {
        Company company = companyRepository.findByCompanyEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        CompanyDetailsDTO companyDetailsDTO = new CompanyDetailsDTO();
        companyDetailsDTO.setCompanyEmail(company.getCompanyEmail());
        companyDetailsDTO.setCompanyName(company.getCompanyName());
        companyDetailsDTO.setCompanyPhone(company.getCompanyPhone());
        return companyDetailsDTO;
    }
}
