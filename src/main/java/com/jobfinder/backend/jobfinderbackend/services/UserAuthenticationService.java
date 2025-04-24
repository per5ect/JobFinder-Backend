package com.jobfinder.backend.jobfinderbackend.services;

import com.jobfinder.backend.jobfinderbackend.dto.LoginUserDTO;
import com.jobfinder.backend.jobfinderbackend.dto.RegisterUserDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VerifyUserDTO;
import com.jobfinder.backend.jobfinderbackend.models.Role;
import com.jobfinder.backend.jobfinderbackend.models.RoleEnum;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.repository.RoleRepository;
import com.jobfinder.backend.jobfinderbackend.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class UserAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public UserAuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signUp(RegisterUserDTO input) {
        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        Role userRole = roleRepository.findByRoleName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role User is not found"));


        System.out.println("Начинаем регистрацию: " + input.getEmail());

        User user = new User(input.getEmail(), passwordEncoder.encode(input.getPassword()), input.getFirstName(), input.getLastName());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiredAt(LocalDateTime.now().plusMinutes(15));
        user.setRoles(Set.of(userRole));
        user.setEnabled(false);

        System.out.println("Сохраняем пользователя в базу...");
        User savedUser = userRepository.save(user);
        System.out.println("Пользователь сохранен с ID: " + savedUser.getId());

        System.out.println("Отправляем письмо...");
        sendVerificationEmail(savedUser);
        System.out.println("Письмо отправлено!");

        return savedUser;
    }

    public User logIn(LoginUserDTO input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account is not verified, please verify your account");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword())
        );
        return user;
    }

    public void verifyUser(VerifyUserDTO input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getVerificationCodeExpiredAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code is expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiredAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User Not Found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.isEnabled()) {
                throw new RuntimeException("Account is verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiredAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User Not Found");
        }
    }

    public void sendVerificationEmail(User user){
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
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
            emailService.sendVerificationCode(user.getEmail(), subject, htmlMessage);
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



