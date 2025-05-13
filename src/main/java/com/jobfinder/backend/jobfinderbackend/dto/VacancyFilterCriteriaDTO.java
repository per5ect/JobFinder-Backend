package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VacancyFilterCriteriaDTO {
    private String jobTitle;
    private String jobLocation;
    private Integer workExperience;
    private String typeOfEmployment;
    private String workMode;
    private String knowledgeLevel;
}
