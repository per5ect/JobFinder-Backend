package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.UserDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VacancyDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.models.Technology;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.models.UserFavoriteVacancy;
import com.jobfinder.backend.jobfinderbackend.models.Vacancy;
import com.jobfinder.backend.jobfinderbackend.repository.UserFavoriteVacancyRepository;
import com.jobfinder.backend.jobfinderbackend.repository.UserRepository;
import com.jobfinder.backend.jobfinderbackend.repository.VacancyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFavoriteVacancyRepository userFavoriteVacancyRepository;
    private final VacancyRepository vacancyRepository;


    public UserDetailsDTO getUserDetails(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isEnabled()
        );

        return userDetailsDTO;
    }

    public void addFavoriteVacancy(Long vacancyId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        if (userFavoriteVacancyRepository.existsByUserAndVacancy(user, vacancy)) {
            throw new RuntimeException("User already added this vacancy to favorites");
        }

        UserFavoriteVacancy userFavoriteVacancy = new UserFavoriteVacancy();

        userFavoriteVacancy.setUser(user);
        userFavoriteVacancy.setVacancy(vacancy);

        userFavoriteVacancyRepository.save(userFavoriteVacancy);
    }

    @Transactional
    public void removeFavoriteVacancy(Long vacancyId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        userFavoriteVacancyRepository.deleteByUserAndVacancy(user, vacancy);
    }

    public List<VacancyDetailsDTO> getUserFavoriteVacancies(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userFavoriteVacancyRepository.findByUser(user).stream()
                .map(fav -> {
                    Vacancy v = fav.getVacancy();
                    return new VacancyDetailsDTO(
                            v.getId(),
                            v.getTitle(),
                            v.getShortDescription(),
                            v.getLocation(),
                            v.getKnowledgeLevel(),
                            v.getTypeOfEmployment(),
                            v.getWorkExperience(),
                            v.getWorkMode(),
                            v.getSalary(),
                            v.getCompany().getCompanyName(),
                            v.getCompany().getCompanyEmail(),
                            v.getCompany().getCompanyPhone(),
                            v.getRequirements(),
                            v.getOfferings(),
                            v.getTechnologies().stream().map(Technology::getTechnologyName).collect(Collectors.toSet())
                    );
                }).collect(Collectors.toList());
    }

    public void updateUserData(String name, String surname, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (name != null) {
            user.setFirstName(name);
        }

        if (surname != null) {
            user.setLastName(surname);
        }

        userRepository.save(user);
    }
}
