package com.jobfinder.backend.jobfinderbackend.repository;

import com.jobfinder.backend.jobfinderbackend.models.Company;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.models.Vacancy;
import com.jobfinder.backend.jobfinderbackend.models.VacancyApplications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VacancyApplicationsRepository extends JpaRepository<VacancyApplications, Long> {
    List<VacancyApplications> findByVacancy_Company(Company vacancyCompany);

    List<VacancyApplications> findByUser(User user);

    Optional<VacancyApplications> findByIdAndVacancy_Company(Long id, Company company);

    boolean existsByVacancyAndUser(Vacancy vacancy, User user);

    List<VacancyApplications> findByVacancy(Vacancy vacancy);
}
