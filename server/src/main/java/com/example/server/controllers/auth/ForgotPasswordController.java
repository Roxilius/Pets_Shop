package com.example.server.controllers.auth;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
// import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.data_transfer_object.auth.ChangePasswordRequest;
// import com.example.server.data_transfer_object.auth.ChangePasswordRequest;
import com.example.server.models.ForgotPassword;
import com.example.server.models.Users;
import com.example.server.repositorys.ForgotPasswordRepository;
import com.example.server.repositorys.UsersRepository;
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
    // send email for email verification
    @SuppressWarnings("null")
    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        Users user = usersRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Email Tidak Ditemukan!!"));

        int otp = otpGenereted();        
        ForgotPassword fp = ForgotPassword.builder()
            .otp(otp)
            .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
            .user(user)
            .build();

        sendEmail(email, otp);
        forgotPasswordRepository.save(fp);
        return ResponseEntity.ok("Email Send For Verification");
    }
        
    @SuppressWarnings("null")
    @PostMapping("/verify-otp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        Users user = usersRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Email Tidak Ditemukan!!"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
        .orElseThrow(() -> new UsernameNotFoundException("Invalid OTP for Email : " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getId());
            return new ResponseEntity<>("OTP has Expired", HttpStatus.EXPECTATION_FAILED);
        }
        forgotPasswordRepository.deleteById(fp.getId());
        return ResponseEntity.ok("OTP Verified!!");
    }

    private void sendEmail(String to, int otp) {
        String subject = "Student Registration";
        String text = "Kode OTP untuk lupa password anda : " + otp;
        emailService.sendSimpleMessage(to, subject, text);
    }

    private Integer otpGenereted(){
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    @PostMapping("/change-password/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest request, @PathVariable String email){
        if (!Objects.equals(request.getPassword(), request.getRePassword())) {
            return new ResponseEntity<>("Please Enter the Password Again", HttpStatus.EXPECTATION_FAILED);
        }

        String password = passwordEncoder.encode(request.getPassword());
        usersRepository.updatePassword(password, email);

        return ResponseEntity.ok("Password has be Changed!");
    }
}

