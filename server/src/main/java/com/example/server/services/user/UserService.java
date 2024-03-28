package com.example.server.services.user;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.data_transfer_object.user.ChangePasswordRequest;
import com.example.server.data_transfer_object.user.LoginRequest;
import com.example.server.data_transfer_object.user.LoginResponse;
import com.example.server.data_transfer_object.user.Register;

public interface UserService {
    Register register(Register request);
    void uploadUserImage(String UserId,MultipartFile userImage) throws IOException, SQLException;
    LoginResponse login(LoginRequest request);

    void verifyEmail(@PathVariable String email);
    void verifyOtp(@PathVariable Integer otp, @PathVariable String email);
    void changePasswordHandler(@RequestBody ChangePasswordRequest request, @PathVariable String email);
}
