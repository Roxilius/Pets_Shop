package com.example.server.services.mahasiswa;

import com.example.server.data_transfer_object.MahasiswaRequestDto;
import com.example.server.models.Mahasiswa;

public interface RegistrationService {
    Mahasiswa register(MahasiswaRequestDto dto);
}
