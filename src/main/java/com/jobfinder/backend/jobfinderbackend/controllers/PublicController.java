package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.TechnologyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VacancyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VacancyFilterCriteriaDTO;
import com.jobfinder.backend.jobfinderbackend.services.TechnologyService;
import com.jobfinder.backend.jobfinderbackend.services.VacancyFilterService;
import com.jobfinder.backend.jobfinderbackend.services.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final VacancyService vacancyService;
    private final TechnologyService technologyService;
    private final VacancyFilterService vacancyFilterService;

    @GetMapping("/all-vacancies")
    public ResponseEntity<List<VacancyDetailsDTO>> getAllVacancies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer workExperience,
            @RequestParam(required = false) String typeOfEmployment,
            @RequestParam(required = false) String workMode,
            @RequestParam(required = false) String knowledgeLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        VacancyFilterCriteriaDTO criteria = new VacancyFilterCriteriaDTO();
        criteria.setJobTitle(title);
        criteria.setJobLocation(location);
        criteria.setWorkExperience(workExperience);
        criteria.setTypeOfEmployment(typeOfEmployment);
        criteria.setWorkMode(workMode);
        criteria.setKnowledgeLevel(knowledgeLevel);

        List<VacancyDetailsDTO> vacancies = vacancyService.getAllVacancies();

        List<VacancyDetailsDTO> filteredVacancies = vacancyFilterService.filterVacancies(vacancies, criteria);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filteredVacancies.size());

        if (fromIndex >= filteredVacancies.size()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<VacancyDetailsDTO> paginatedVacancies = filteredVacancies.subList(fromIndex, toIndex);

        return ResponseEntity.ok(paginatedVacancies);
    }

    @GetMapping("/all-vacancies/{id}")
    public ResponseEntity<VacancyDetailsDTO> getVacancyById(@PathVariable Long id){
        VacancyDetailsDTO vacancyDetails = vacancyService.getVacancyById(id);
        return ResponseEntity.ok(vacancyDetails);
    }

    @GetMapping("/all-technology")
    public ResponseEntity<List<TechnologyDTO>> getAllTechnologies(){
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

}
