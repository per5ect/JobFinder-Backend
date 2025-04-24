package com.jobfinder.backend.jobfinderbackend.controllers;

import com.jobfinder.backend.jobfinderbackend.dto.*;
import com.jobfinder.backend.jobfinderbackend.models.Company;
import com.jobfinder.backend.jobfinderbackend.responses.LoginResponse;
import com.jobfinder.backend.jobfinderbackend.services.CompanyAuthenticationService;
import com.jobfinder.backend.jobfinderbackend.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth/company")
@RestController
@RequiredArgsConstructor
public class CompanyAuthenticationController {
    private final JWTService jwtService;
    private final CompanyAuthenticationService companyAuthenticationService;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterCompanyDTO input) {
        System.out.println(">>> Получен запрос на регистрацию: " + input.getCompanyEmail());
        Company company = companyAuthenticationService.signUp(input);
        return ResponseEntity.ok(company);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginCompanyDTO input){
        Company loginedCompany = companyAuthenticationService.logIn(input);
        String jwtToken = jwtService.generateToken(loginedCompany);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCompany(@RequestBody VerifyCompanyDTO verifyCompanyDto){
        try{
            companyAuthenticationService.verifyCompany(verifyCompanyDto);
            return ResponseEntity.ok("Company verified");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try{
            companyAuthenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
