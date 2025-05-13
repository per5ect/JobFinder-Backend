package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.ResponsesToVacancyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.UserApplicationsDTO;
import com.jobfinder.backend.jobfinderbackend.models.*;
import com.jobfinder.backend.jobfinderbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyApplicationsService {

    private final VacancyApplicationsRepository vacancyApplicationsRepository;
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;
    private final CompanyRepository companyRepository;
    private final ResumeRepository resumeRepository;

    public void applyToVacancy(Long vacancyId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        if (vacancyApplicationsRepository.existsByVacancyAndUser(vacancy, user)) {
            throw new RuntimeException("User already applied to this vacancy");
        }

        VacancyApplications vacancyApplications = new VacancyApplications();
        vacancyApplications.setUser(user);
        vacancyApplications.setVacancy(vacancy);

        vacancyApplicationsRepository.save(vacancyApplications);
    }

    public List<UserApplicationsDTO> getUsersApplications(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<VacancyApplications> vacancyApplications = vacancyApplicationsRepository.findByUser(user);

        return vacancyApplications.stream()
                .map(application -> new UserApplicationsDTO(
                        application.getVacancy().getId(),
                        application.getVacancy().getTitle(),
                        application.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    public List<ResponsesToVacancyDTO> getApplicationsByVacancyId(Long vacancyId, String companyEmail) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        List<VacancyApplications> applications = vacancyApplicationsRepository.findByVacancy(vacancy);

        return applications.stream()
                .map(
                        application -> {
                            Resume resume = resumeRepository.findByUser(application.getUser()).orElse(null);
                            String resumeLink = (resume != null) ? resume.getCloudLink() : null;

                            return new ResponsesToVacancyDTO(
                                    application.getId(),
                                    application.getUser().getFirstName(),
                                    application.getUser().getLastName(),
                                    application.getUser().getEmail(),
                                    resumeLink,
                                    application.getStatus().name()
                            );
                        })
                .collect(Collectors.toList());
    }

    public void updateApplicationStatus(Long applicationId, String companyEmail, ApplicationStatusEnum newStatus) {
        VacancyApplications vacancyApplications = vacancyApplicationsRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        vacancyApplications.setStatus(newStatus);
        vacancyApplicationsRepository.save(vacancyApplications);
    }
}
