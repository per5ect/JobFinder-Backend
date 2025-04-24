package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginCompanyDTO {
    private String companyEmail;
    private String password;
}
