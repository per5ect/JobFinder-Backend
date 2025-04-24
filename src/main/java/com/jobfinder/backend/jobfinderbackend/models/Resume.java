package com.jobfinder.backend.jobfinderbackend.models;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cloudLink;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> techStackFromCV;

    @Column(name = "experience_years")
    private String experienceYears;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
