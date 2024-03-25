package com.example.server.data_transfer_object.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersRegistrationRequestDto {
    // profile user
    String fullName;
    String email;
    String dateOfBirth; //yyyy-MM-dd ISO_8601
    String address;
    String phoneNumber;
    String gender;
    String userPhoto;

    // user
    String userName;
    String password;
}
