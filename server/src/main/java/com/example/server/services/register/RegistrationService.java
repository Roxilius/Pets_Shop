package com.example.server.services.register;

import com.example.server.data_transfer_object.user.UsersRegistrationRequestDto;
import com.example.server.models.UsersProfile;

public interface RegistrationService {
    UsersProfile register(UsersRegistrationRequestDto dto);
}
