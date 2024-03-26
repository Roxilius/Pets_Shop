package com.example.server.services.auth;

import com.example.server.data_transfer_object.auth.LoginRequest;
import com.example.server.data_transfer_object.auth.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);
}
