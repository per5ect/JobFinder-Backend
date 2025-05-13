package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponsesToVacancyDTO {
    private Long applicationId;
    private String applicantFirstName;
    private String applicantLastName;
    private String applicantEmail;
    private String resumeLink;
    private String applicationStatus;
}
