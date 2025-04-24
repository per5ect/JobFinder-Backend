package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDetailsDTO {
    private Long id;
    private String title;
    private String shortDescription;
    private String location;
    private String knowledgeLevel;
    private String typeOfEmployment;
    private Integer workExperience;
    private String workMode;
    private String salary;

    private String companyName;
    private String companyEmail;
    private String companyPhone;

    private List<String> requirements;
    private List<String> offerings;

    private Set<String> technologyNames;

}
