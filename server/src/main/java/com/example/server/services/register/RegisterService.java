package com.example.server.services.register;

import com.example.server.data_transfer_object.register.RegisterRequest;
import com.example.server.models.Users;

public interface RegisterService {
    Users register(RegisterRequest request);
}
