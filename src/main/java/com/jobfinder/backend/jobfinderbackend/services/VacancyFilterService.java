package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.VacancyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VacancyFilterCriteriaDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacancyFilterService {

    public List<VacancyDetailsDTO> filterVacancies(List<VacancyDetailsDTO> vacancies, VacancyFilterCriteriaDTO criteria) {
        return vacancies.stream()
                .filter(v -> criteria.getJobTitle() == null || v.getTitle().toLowerCase().contains(criteria.getJobTitle().toLowerCase()))
                .filter(v -> criteria.getJobLocation() == null || v.getLocation().equalsIgnoreCase(criteria.getJobLocation()))
                .filter(v -> criteria.getWorkExperience() == null || v.getWorkExperience() <= criteria.getWorkExperience())
                .filter(v -> criteria.getTypeOfEmployment() == null || v.getTypeOfEmployment().equalsIgnoreCase(criteria.getTypeOfEmployment()))
                .filter(v -> criteria.getWorkMode() == null || v.getWorkMode().equalsIgnoreCase(criteria.getWorkMode()))
                .filter(v -> criteria.getKnowledgeLevel() == null || v.getKnowledgeLevel().equalsIgnoreCase(criteria.getKnowledgeLevel()))
                .collect(Collectors.toList());
    }
}
