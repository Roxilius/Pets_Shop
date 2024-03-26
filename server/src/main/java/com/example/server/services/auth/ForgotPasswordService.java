package com.example.server.services.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.server.data_transfer_object.auth.ChangePasswordRequest;

public interface ForgotPasswordService {
    void verifyEmail(@PathVariable String email);
    ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email);
    ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest request, @PathVariable String email);
}
