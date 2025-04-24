package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCompanyDTO {
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String password;
}
