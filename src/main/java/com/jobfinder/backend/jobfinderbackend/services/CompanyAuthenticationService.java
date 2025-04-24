package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.LoginCompanyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.RegisterCompanyDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VerifyCompanyDTO;
import com.jobfinder.backend.jobfinderbackend.models.Company;
import com.jobfinder.backend.jobfinderbackend.models.Role;
import com.jobfinder.backend.jobfinderbackend.models.RoleEnum;
import com.jobfinder.backend.jobfinderbackend.repository.CompanyRepository;
import com.jobfinder.backend.jobfinderbackend.repository.RoleRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class CompanyAuthenticationService {
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final AuthenticationProvider companyAuthenticationProvider;

    public CompanyAuthenticationService(CompanyRepository companyRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, AuthenticationProvider companyAuthenticationProvider) {
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.companyAuthenticationProvider = companyAuthenticationProvider;
    }

    public Company signUp(RegisterCompanyDTO input) {
        if(companyRepository.findByCompanyEmail(input.getCompanyEmail()).isPresent()) {
            throw new RuntimeException("Company with this email already exists");
        }

        Role companyRole = roleRepository.findByRoleName(RoleEnum.ROLE_COMPANY)
                .orElseThrow(() -> new RuntimeException("Role Company is not found"));

        System.out.println("Начинаем регистрацию: " + input.getCompanyEmail());

        Company company = new Company(input.getCompanyEmail(), passwordEncoder.encode(input.getPassword()), input.getCompanyName(), input.getCompanyPhone());
        company.setVerificationCode(generateVerificationCode());
        company.setVerificationCodeExpiredAt(LocalDateTime.now().plusMinutes(15));
        company.setRoles(Set.of(companyRole));
        company.setEnabled(false);

        System.out.println("Сохраняем company в базу...");
        Company savedCompany = companyRepository.save(company);
        System.out.println("Пользователь сохранен с ID: " + savedCompany.getId());

        System.out.println("Отправляем письмо...");
        sendVerificationEmail(savedCompany);
        System.out.println("Письмо отправлено!");

        return savedCompany;
    }

    public Company logIn(LoginCompanyDTO input) {
        System.out.println(input.getCompanyEmail());
        Company company = companyRepository.findByCompanyEmail(input.getCompanyEmail())
                .orElseThrow(() -> new RuntimeException("Company Not Found"));

        if(!company.isEnabled()) {
            throw new RuntimeException("Account is not verified, please verify your account");
        }
        companyAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getCompanyEmail(),
                        input.getPassword())
        );
        return company;
    }

    public void verifyCompany(VerifyCompanyDTO input) {
        System.out.println("Email for verification: " + input.getCompanyEmail());
        Optional<Company> optionalCompany = companyRepository.findByCompanyEmail(input.getCompanyEmail());
        if(optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            if(company.getVerificationCodeExpiredAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Company Verification code is Expired");
            }
            if(company.getVerificationCode().equals(input.getVerificationCode())) {
                company.setEnabled(true);
                company.setVerificationCode(null);
                company.setVerificationCodeExpiredAt(null);
                companyRepository.save(company);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("Company Not Found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<Company> optionalCompany = companyRepository.findByCompanyEmail(email);
        if(optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            if(company.isEnabled()) {
                throw new RuntimeException("Account is verified");
            }
            company.setVerificationCode(generateVerificationCode());
            company.setVerificationCodeExpiredAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(company);
            companyRepository.save(company);
        }
    }

    public void sendVerificationEmail(Company company) {
        String subject = "Account Verification";
        String verificationCode = company.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px; text-align: center;\">"
                + "<div style=\"max-width: 500px; margin: auto; background-color: #ffffff; padding: 30px;"
                + "border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center;\">"
                + "<h2 style=\"color: #000; font-size: 32px; font-weight: bold;\">FIND YOUR JOB</h2>"
                + "<p style=\"font-size: 16px; color: #333;\">To use our platform, please verify your email by entering the code below:</p>"
                + "<div style=\"background-color: #A2E3C4; padding: 15px; border-radius: 5px; display: inline-block; margin: 20px 0;\">"
                + "<h4 style=\"color: #333; margin: 0; font-size: 20px; font-weight: lighter\">Verification Code</h4>"
                + "<p style=\"font-size: 24px; font-weight: bold; color: #222A24; margin: 5px 0;\">" + verificationCode + "</p>"
                + "</div>"
                + "<p style=\"margin-top: 20px; font-size: 14px; color: #777;\">If you didn't request this, please ignore this email.</p>"
                + "<div style=\"margin-top: 30px; padding-top: 10px; border-top: 1px solid #ddd; font-size: 12px; color: #666;\">"
                + "<p>findjob@mail.com</p>"
                + "<p>&copy; 2025 Find Job. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationCode(company.getCompanyEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
