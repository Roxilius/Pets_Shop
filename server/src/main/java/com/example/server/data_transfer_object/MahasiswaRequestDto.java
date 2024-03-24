package com.example.server.data_transfer_object;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MahasiswaRequestDto {
    // mahasiswa
    private String nim;
    private String mahasiswaName;
    private String dateOfBirth; // 2002-10-25 yyyy-MM-dd ISO_8601
    private String placeOfBirth;
    private String gender;
    private String photo;

    // user
    private String userName;
    private String password;
}
