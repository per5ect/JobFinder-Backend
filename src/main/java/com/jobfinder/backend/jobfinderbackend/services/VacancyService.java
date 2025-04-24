package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.CreateVacancyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VacancyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.models.Company;
import com.jobfinder.backend.jobfinderbackend.models.Technology;
import com.jobfinder.backend.jobfinderbackend.models.Vacancy;
import com.jobfinder.backend.jobfinderbackend.repository.TechnologyRepository;
import com.jobfinder.backend.jobfinderbackend.repository.VacancyRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VacancyService {
    private final VacancyRepository vacancyRepository;
    private final TechnologyRepository technologyRepository;

    public VacancyService(VacancyRepository vacancyRepository, TechnologyRepository technologyRepository) {
        this.vacancyRepository = vacancyRepository;
        this.technologyRepository = technologyRepository;
    }

    public void createVacancy(CreateVacancyDTO input, Company company) {

        Set<Technology> technologies = new HashSet<>();

        for (Long techId : input.getTechnologyIds()) {
            Technology tech = technologyRepository.findById(techId)
                    .orElseThrow(() -> new IllegalArgumentException("Technology with ID " + techId + " not find"));
            technologies.add(tech);
        }

        Vacancy vacancy = new Vacancy();

        vacancy.setTitle(input.getTitle());
        vacancy.setShortDescription(input.getShortDescription());
        vacancy.setSalary(input.getSalary());
        vacancy.setLocation(input.getLocation());
        vacancy.setKnowledgeLevel(input.getKnowledgeLevel());
        vacancy.setWorkMode(input.getWorkMode());
        vacancy.setTypeOfEmployment(input.getTypeOfEmployment());
        vacancy.setWorkExperience(input.getWorkExperience());
        vacancy.setRequirements(input.getRequirements());
        vacancy.setOfferings(input.getOfferings());
        vacancy.setTechnologies(technologies);
        vacancy.setCompany(company);

        vacancyRepository.save(vacancy);
    }

    public List<VacancyDetailsDTO> getAllVacancies() {
        List<Vacancy> vacancies = vacancyRepository.findAll();

        return vacancies.stream().map(vacancy -> new VacancyDetailsDTO(
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

                vacancy.getTechnologies().stream().map(Technology::getTechnologyName)
                        .collect(Collectors.toSet())

        )).collect(Collectors.toList());
    }

    public VacancyDetailsDTO getVacancyById(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vacancy with ID " + id + " not find"));

        VacancyDetailsDTO vacancyDetails = new VacancyDetailsDTO();
        vacancyDetails.setTitle(vacancy.getTitle());
        vacancyDetails.setShortDescription(vacancy.getShortDescription());
        vacancyDetails.setLocation(vacancy.getLocation());
        vacancyDetails.setSalary(vacancy.getSalary());
        vacancyDetails.setWorkExperience(vacancy.getWorkExperience());
        vacancyDetails.setKnowledgeLevel(vacancy.getKnowledgeLevel());
        vacancyDetails.setTypeOfEmployment(vacancy.getTypeOfEmployment());
        vacancyDetails.setWorkMode(vacancy.getWorkMode());

        vacancyDetails.setRequirements(vacancy.getRequirements());
        vacancyDetails.setOfferings(vacancy.getOfferings());

        vacancyDetails.setTechnologyNames(
                vacancy.getTechnologies().stream()
                        .map(Technology::getTechnologyName)
                        .collect(Collectors.toSet())
        );

        vacancyDetails.setCompanyName(vacancy.getCompany().getCompanyName());
        vacancyDetails.setCompanyEmail(vacancy.getCompany().getCompanyEmail());
        vacancyDetails.setCompanyPhone(vacancy.getCompany().getCompanyPhone());

        return vacancyDetails;
    }

    public void deleteVacancyById(Long id) {
        vacancyRepository.deleteById(id);
    }
}