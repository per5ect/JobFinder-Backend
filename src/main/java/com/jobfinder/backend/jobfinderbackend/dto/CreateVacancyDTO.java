package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateVacancyDTO {
    private String title;
    private String shortDescription;
    private String salary;
    private String location;
    private String knowledgeLevel;
    private String workMode;
    private String typeOfEmployment;
    private Integer workExperience;
    private List<String> requirements;
    private List<String> offerings;
    private Set<Long> technologyIds;
}
