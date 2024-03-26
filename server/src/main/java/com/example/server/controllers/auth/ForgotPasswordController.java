package com.example.server.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.data_transfer_object.auth.ChangePasswordRequest;
import com.example.server.repositorys.ForgotPasswordRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.auth.ForgotPasswordService;
import com.example.server.services.email.EmailService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/auth")
@RestController
@Tag(name = "user")
public class ForgotPasswordController {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    ForgotPasswordRepository forgotPasswordRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ForgotPasswordService forgotPasswordService;
    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        try {
            forgotPasswordService.verifyEmail(email);
            return ResponseEntity.ok("Cek Your Email For OTP Verification");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
        
    @PostMapping("/verify-otp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        try {
            forgotPasswordService.verifyOtp(otp, email);
            return ResponseEntity.ok("OTP Verified!!");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change-password/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest request, @PathVariable String email){
        try {
            forgotPasswordService.changePasswordHandler(request, email);
            return ResponseEntity.ok("Password has be Changed!");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}