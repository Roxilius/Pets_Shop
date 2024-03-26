package com.example.server.services.auth;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.server.data_transfer_object.auth.ChangePasswordRequest;
import com.example.server.models.ForgotPassword;
import com.example.server.models.Users;
import com.example.server.repositorys.ForgotPasswordRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.email.EmailService;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService{
    @Autowired
    EmailService emailService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    ForgotPasswordRepository forgotPasswordRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @SuppressWarnings("null")
    @Override
    public void verifyEmail(@PathVariable String email){
        Users user = usersRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Email Tidak Ditemukan!!"));
        
        int otp = new Random().nextInt(100_000, 999_999);
        ForgotPassword fp = ForgotPassword.builder()
        .otp(otp)
        .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
        .user(user)
        .build();

        ForgotPassword existFp = forgotPasswordRepository.findByUser(user).orElse(null);
        if (existFp != null ) {
            forgotPasswordRepository.delete(existFp);
        }
        sendEmail(email, otp);
        forgotPasswordRepository.save(fp);
    }
    private void sendEmail(String to, int otp) {
        String subject = "Verify Forgot Password";
        String text = "OTP Code For Forgot Your Password : " + otp;
        emailService.sendSimpleMessage(to, subject, text);
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        Users user = usersRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Email Not Found!!"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
        .orElseThrow(() -> new UsernameNotFoundException("Invalid OTP for Email : " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getId());
            return new ResponseEntity<>("OTP has Expired", HttpStatus.EXPECTATION_FAILED);
        }
        forgotPasswordRepository.deleteById(fp.getId());
        return ResponseEntity.ok("OTP Verified!!");
    }

    @Override
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest request, @PathVariable String email){
        if (!Objects.equals(request.getPassword(), request.getRePassword())) {
            return new ResponseEntity<>("Please Enter the Password Again", HttpStatus.EXPECTATION_FAILED);
        }
        String password = passwordEncoder.encode(request.getPassword());
        usersRepository.updatePassword(password, email);
        return ResponseEntity.ok("Password has be Changed!");
    }
}
