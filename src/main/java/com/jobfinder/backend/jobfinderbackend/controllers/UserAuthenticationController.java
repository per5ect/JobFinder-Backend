package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.LoginUserDTO;
import com.jobfinder.backend.jobfinderbackend.dto.RegisterUserDTO;
import com.jobfinder.backend.jobfinderbackend.dto.VerifyUserDTO;
import com.jobfinder.backend.jobfinderbackend.models.User;
import com.jobfinder.backend.jobfinderbackend.responses.LoginResponse;
import com.jobfinder.backend.jobfinderbackend.services.UserAuthenticationService;
import com.jobfinder.backend.jobfinderbackend.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth/user")
@RestController
@RequiredArgsConstructor
public class UserAuthenticationController {
    private final JWTService jwtService;
    private final UserAuthenticationService authenticationService;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterUserDTO input) {
        System.out.println(">>> Получен запрос на регистрацию: " + input.getEmail());
        User user = authenticationService.signUp(input);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDTO loginUserDto){
        User loginedUser = authenticationService.logIn(loginUserDto);
        String jwtToken = jwtService.generateToken(loginedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO verifyUserDto){
        try{
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("User verified");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try{
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
