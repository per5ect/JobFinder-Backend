package com.jobfinder.backend.jobfinderbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_favorite_vacancies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteVacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Vacancy vacancy;
}
