package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.ResumeAnalysisResponseDTO;
import com.jobfinder.backend.jobfinderbackend.dto.UserDetailsDTO;
import com.jobfinder.backend.jobfinderbackend.models.Resume;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.services.ResumeAnalysisService;
import com.jobfinder.backend.jobfinderbackend.services.ResumeService;
import com.jobfinder.backend.jobfinderbackend.services.UserService;
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
import java.util.List;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ResumeService resumeService;
    private final ResumeAnalysisService resumeAnalysisService;


    @GetMapping("/my-account")
    public ResponseEntity<UserDetailsDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем email из токена

        UserDetailsDTO userDetails = userService.getUserDetails(email);
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> allUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
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


    @PostMapping("/analyzeCV")
    public ResponseEntity<ResumeAnalysisResponseDTO> analyzeResume(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResumeAnalysisResponseDTO response = resumeAnalysisService.analyzeResume(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

}
