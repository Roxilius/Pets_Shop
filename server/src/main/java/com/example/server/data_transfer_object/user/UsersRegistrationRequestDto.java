package com.example.server.data_transfer_object.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersRegistrationRequestDto {
    // profile user
    private String fullName;
    private String email;
    private String dateOfBirth; //yyyy-MM-dd ISO_8601
    private String address;
    private String phoneNumber;
    private String gender;
    private String userPhoto;

    // user
    private String userName;
    private String password;
}
