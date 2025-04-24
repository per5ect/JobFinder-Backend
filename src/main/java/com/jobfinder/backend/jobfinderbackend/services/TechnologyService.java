package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.CreateTechnologyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.TechnologyDTO;
import com.jobfinder.backend.jobfinderbackend.models.Technology;
import com.jobfinder.backend.jobfinderbackend.repository.TechnologyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyService {
    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    public void createTechnology(CreateTechnologyDTO input) {
        if (technologyRepository.existsByTechnologyNameIgnoreCase(input.getTechnologyName())) {
            throw new RuntimeException("Technology already exists");
        }

        Technology technology = new Technology();
        technology.setTechnologyName(input.getTechnologyName());

        technologyRepository.save(technology);
    }

    public List<TechnologyDTO> getAllTechnologies() {
        return technologyRepository.findAll().stream()
                .map(technology -> new TechnologyDTO(technology.getId(), technology.getTechnologyName()))
                .toList();
    }

    public void deleteTechnologyById(Long id) {
        technologyRepository.deleteById(id);
    }
}
