package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.TechnologyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VacancyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.services.TechnologyService;
import com.jobfinder.backend.jobfinderbackend.services.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private final VacancyService vacancyService;
    private final TechnologyService technologyService;

    @GetMapping("/all-vacancies")
    public ResponseEntity<List<VacancyDetailsDTO>> getAllVacancies(){
       List<VacancyDetailsDTO> vacancies = vacancyService.getAllVacancies();
       return ResponseEntity.ok(vacancies);
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
