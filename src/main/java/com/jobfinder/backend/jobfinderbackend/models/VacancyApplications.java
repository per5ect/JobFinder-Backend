package com.jobfinder.backend.jobfinderbackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vacancy_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacancyApplications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Vacancy vacancy;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ApplicationStatusEnum status;

    @Column(length = 500)
    private String coverLetter;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = ApplicationStatusEnum.PENDING;
    }
}
