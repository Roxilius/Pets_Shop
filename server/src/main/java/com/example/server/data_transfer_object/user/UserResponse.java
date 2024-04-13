package com.example.server.data_transfer_object.user;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private String gender;
    private String image;
}
