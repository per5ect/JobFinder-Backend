package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCompanyDTO {
    private String companyEmail;
    private String verificationCode;
}
