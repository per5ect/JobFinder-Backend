package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResumeAnalysisResponseDTO {
    private List<String> techStack;
    private String experienceYears;
}
