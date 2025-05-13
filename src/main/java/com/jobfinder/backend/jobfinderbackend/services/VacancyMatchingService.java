package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.VacancyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.models.Resume;
import com.jobfinder.backend.jobfinderbackend.models.Technology;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.models.Vacancy;
import com.jobfinder.backend.jobfinderbackend.repository.ResumeRepository;
import com.jobfinder.backend.jobfinderbackend.repository.UserRepository;
import com.jobfinder.backend.jobfinderbackend.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyMatchingService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;

    public List<VacancyDetailsDTO> getMatchingVacancies(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        List<String> techStack = resume.getTechStackFromCV().stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .toList();

        int experience = 0;
        try {
            experience = Integer.parseInt(resume.getExperienceYears());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid experience years");
        }

        List<Vacancy> vacancies = vacancyRepository.findMatchingVacanciesWithExperience(techStack, experience);



        return vacancies.stream()
                .filter(vacancy -> hasSufficientTechnologyMatch(vacancy.getTechnologies(), techStack))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private boolean hasSufficientTechnologyMatch(Set<Technology> vacancyTechnologies, List<String> userTechStack) {
        List<String> vacancyTechNames = vacancyTechnologies.stream()
                .map(tech -> tech.getTechnologyName().toLowerCase().trim())
                .toList();

        // Подсчитываем совпадения по принципу "частичного соответствия"
        long matchCount = vacancyTechNames.stream()
                .filter(vacTech -> userTechStack.stream()
                        .anyMatch(userTech -> userTech.contains(vacTech) || vacTech.contains(userTech)))
                .count();

        double matchPercentage = (double) matchCount / vacancyTechNames.size();

        return matchPercentage >= 0.3; // Порог можно изменить при желании
    }


    private VacancyDetailsDTO mapToDTO(Vacancy vacancy) {
        return new VacancyDetailsDTO(
                vacancy.getId(),
                vacancy.getTitle(),
                vacancy.getShortDescription(),
                vacancy.getLocation(),
                vacancy.getKnowledgeLevel(),
                vacancy.getTypeOfEmployment(),
                vacancy.getWorkExperience(),
                vacancy.getWorkMode(),
                vacancy.getSalary(),
                vacancy.getCompany().getCompanyName(),
                vacancy.getCompany().getCompanyEmail(),
                vacancy.getCompany().getCompanyPhone(),
                vacancy.getRequirements(),
                vacancy.getOfferings(),
                vacancy.getTechnologies().stream().map(Technology::getTechnologyName).collect(Collectors.toSet())
        );
    }
}
