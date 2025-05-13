package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.*;
import com.jobfinder.backend.jobfinderbackend.models.Resume;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.models.UserFavoriteVacancy;
import com.jobfinder.backend.jobfinderbackend.models.VacancyApplications;
import com.jobfinder.backend.jobfinderbackend.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResumeService resumeService;
    private final ResumeAnalysisService resumeAnalysisService;
    private final VacancyMatchingService vacancyMatchingService;
    private final VacancyFilterService vacancyFilterService;
    private final VacancyApplicationsService vacancyApplicationsService;

    @GetMapping("/my-account")
    public ResponseEntity<UserDetailsDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserDetailsDTO userDetails = userService.getUserDetails(email);
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/uploadCV")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication authentication){
        try{
            String userEmail = authentication.getName();
            resumeService.uploadResume(file, userEmail);
            return ResponseEntity.ok("Resume saved successfully");
        } catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading resume");
        }
    }

    @DeleteMapping("/deleteCV")
    public ResponseEntity<?> deleteResume(@AuthenticationPrincipal User user){
        resumeService.deleteResume(user.getEmail());
        return ResponseEntity.ok("Resume deleted successfully");
    }

    @GetMapping("/getCV")
    public ResponseEntity<String> getResumeLink(@AuthenticationPrincipal User user) {
        String link = resumeService.getResumeLink(user.getEmail());
        return ResponseEntity.ok(link);
    }

    @GetMapping("/getCvDetails")
    public ResponseEntity<ResumeAnalysisResponseDTO> getResumeDetails(@AuthenticationPrincipal User user){
        ResumeAnalysisResponseDTO resumeDetails = resumeService.getResumeDetails(user.getEmail());
        return ResponseEntity.ok(resumeDetails);
    }

    @PostMapping("/analyzeCV")
    public ResponseEntity<ResumeAnalysisResponseDTO> analyzeResume(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResumeAnalysisResponseDTO response = resumeAnalysisService.analyzeResume(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getMatchingVacancies")
    public ResponseEntity<List<VacancyDetailsDTO>> getMatchingVacancies(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer workExperience,
            @RequestParam(required = false) String typeOfEmployment,
            @RequestParam(required = false) String workMode,
            @RequestParam(required = false) String knowledgeLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        VacancyFilterCriteriaDTO criteria = new VacancyFilterCriteriaDTO();
        criteria.setJobTitle(title);
        criteria.setJobLocation(location);
        criteria.setWorkExperience(workExperience);
        criteria.setTypeOfEmployment(typeOfEmployment);
        criteria.setWorkMode(workMode);
        criteria.setKnowledgeLevel(knowledgeLevel);

        List<VacancyDetailsDTO> matchingVacancies = vacancyMatchingService.getMatchingVacancies(user.getUsername());

        List<VacancyDetailsDTO> filteredVacancies = vacancyFilterService.filterVacancies(matchingVacancies, criteria);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filteredVacancies.size());

        if (fromIndex >= filteredVacancies.size()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<VacancyDetailsDTO> paginatedVacancies = filteredVacancies.subList(fromIndex, toIndex);

        return ResponseEntity.ok(paginatedVacancies);
    }

    @PostMapping("/apply/{vacancyId}")
    public ResponseEntity<String> applyToVacancy(
            @AuthenticationPrincipal User user,
            @PathVariable Long vacancyId
            ){
        vacancyApplicationsService.applyToVacancy(vacancyId, user.getUsername());
        return ResponseEntity.ok("Application sent successfully");
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<UserApplicationsDTO>> getMyApplications(
            @AuthenticationPrincipal User user
    ){
        List<UserApplicationsDTO> myApplications = vacancyApplicationsService.getUsersApplications(user.getUsername());
        return ResponseEntity.ok(myApplications);
    }

    @PostMapping("/add-to-favorite/{vacancyId}")
    public ResponseEntity<String> addToFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable Long vacancyId
    ){
        userService.addFavoriteVacancy(vacancyId ,user.getUsername());
        return ResponseEntity.ok("Vacancy added to favorites");
    }

    @DeleteMapping("remove-from-favorite/{vacancyId}")
    public ResponseEntity<String> removeFromFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable Long vacancyId
    ){
        userService.removeFavoriteVacancy(vacancyId ,user.getUsername());
        return ResponseEntity.ok("Vacancy removed from favorites");
    }

    @GetMapping("/my-favorite-vacancies")
    public ResponseEntity<List<VacancyDetailsDTO>> getMyFavoriteVacancies(
            @AuthenticationPrincipal User user
    ){
        List<VacancyDetailsDTO> favorites = userService.getUserFavoriteVacancies(user.getUsername());
        return ResponseEntity.ok(favorites);
    }

    @PatchMapping("/my-account/update-personal-info")
    public ResponseEntity<String> updatePersonalInformation(
            @AuthenticationPrincipal User user,
            @RequestBody(required = false) String name,
            @RequestBody(required = false) String surname
    ){
        userService.updateUserData(name, surname, user.getUsername());
        System.out.println("data:");
        System.out.println(name);
        System.out.println(surname);
        return ResponseEntity.ok("Personal information updated successfully");
    }
}

