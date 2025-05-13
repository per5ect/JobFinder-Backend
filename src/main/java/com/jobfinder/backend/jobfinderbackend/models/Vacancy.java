package com.jobfinder.backend.jobfinderbackend.models;

import io.hypersistence.utils.hibernate.type.json.JsonType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vacancies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 300)
    private String shortDescription;

    private String salary;

    private String location;

    private String knowledgeLevel;

    private String workMode;

    private String typeOfEmployment;

    private Integer workExperience;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> requirements;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> offerings;


    @ManyToMany
    @JoinTable(
            name = "vacancy_technologies",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<Technology> technologies;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VacancyApplications> applications = new ArrayList<>();
}
