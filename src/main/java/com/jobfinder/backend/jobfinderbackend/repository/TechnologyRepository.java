package com.jobfinder.backend.jobfinderbackend.repository;

import com.jobfinder.backend.jobfinderbackend.models.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    Optional<Technology> findByTechnologyNameIgnoreCase(String technologyName);
    Boolean existsByTechnologyNameIgnoreCase(String technologyName);
    void deleteById(Long id);
}
