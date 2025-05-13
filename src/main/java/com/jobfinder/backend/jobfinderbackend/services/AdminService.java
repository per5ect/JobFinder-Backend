package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.CompanyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.dto.UserDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.repository.CompanyRepository;
import com.jobfinder.backend.jobfinderbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public List<UserDetailsDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDetailsDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.isEnabled()
                )).toList();
    }

    public List<CompanyDetailsDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(company -> new CompanyDetailsDTO(
                        company.getId(),
                        company.getCompanyEmail(),
                        company.getCompanyName(),
                        company.getCompanyPhone(),
                        company.isEnabled()
                )).toList();

        }
}
