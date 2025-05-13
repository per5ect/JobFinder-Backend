package com.jobfinder.backend.jobfinderbackend.repository;

import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.models.UserFavoriteVacancy;
import com.jobfinder.backend.jobfinderbackend.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteVacancyRepository extends JpaRepository<UserFavoriteVacancy, Long> {
    boolean existsByUserAndVacancy(User user, Vacancy vacancy);

    List<UserFavoriteVacancy> findByUser(User user);

    void deleteByUserAndVacancy(User user, Vacancy vacancy);
}
