package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.ResumeAnalysisResponseDTO;
import com.jobfinder.backend.jobfinderbackend.models.Resume;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.repository.ResumeRepository;
import com.jobfinder.backend.jobfinderbackend.repository.UserRepository;
import com.jobfinder.backend.jobfinderbackend.utils.GeminiClient;
import com.jobfinder.backend.jobfinderbackend.utils.ResumeParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
public class ResumeAnalysisService {

    private final ResumeParser resumeParser;
    private final GeminiClient geminiClient;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeAnalysisResponseDTO analyzeResume(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Resume not found"));

        String resumeText = resumeParser.extractTextFromPDF(resume.getCloudLink());
        System.out.println(resumeText);
        String geminiResponse = geminiClient.analyzeResumeText(resumeText);

        List<String> splitResponse = Arrays.stream(geminiResponse.split(","))
                .map(el -> el.trim().replace("\n", ""))
                .filter(el2 -> !el2.isEmpty())
                .toList();

        if(splitResponse.size() < 2) throw new RuntimeException("Invalid gemini response");

        String experience;
        String lastElement = splitResponse.get(splitResponse.size() - 1);

        if (parseInt(lastElement) > 5) {
            experience = "5";
        } else {
            experience = lastElement;
        }

        List<String> techStack = splitResponse.subList(0, splitResponse.size() - 1);
        System.out.println(techStack);

        resume.setTechStackFromCV(techStack);
        resume.setExperienceYears(experience);
        resumeRepository.save(resume);

        return new ResumeAnalysisResponseDTO(techStack, experience);
    }

}
