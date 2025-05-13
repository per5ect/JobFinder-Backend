package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyDetailsDTO {
    private Long id;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private Boolean enabled;
}
