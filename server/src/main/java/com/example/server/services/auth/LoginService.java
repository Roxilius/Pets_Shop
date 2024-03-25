package com.example.server.services.auth;

import com.example.server.data_transfer_object.auth.LoginRequestDto;
import com.example.server.data_transfer_object.auth.LoginResponseDto;

public interface LoginService {
    LoginResponseDto login(LoginRequestDto dto);
}
