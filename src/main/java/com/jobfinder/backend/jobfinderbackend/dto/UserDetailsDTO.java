package com.jobfinder.backend.jobfinderbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
}
