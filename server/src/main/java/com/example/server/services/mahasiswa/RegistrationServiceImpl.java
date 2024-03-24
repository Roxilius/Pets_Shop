package com.example.server.services.mahasiswa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.server.constants.RolesConstant;
import com.example.server.data_transfer_object.MahasiswaRequestDto;
import com.example.server.models.Mahasiswa;
import com.example.server.models.Roles;
import com.example.server.models.Users;
import com.example.server.repositorys.MahasiswaRepository;
import com.example.server.repositorys.RolesRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.email.EmailService;

import jakarta.transaction.Transactional;

@Service
public class RegistrationServiceImpl implements RegistrationService{
    @Autowired
    EmailService emailSevice;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    MahasiswaRepository mahasiswaRepository;
    @Transactional
    @Override
    public Mahasiswa register(MahasiswaRequestDto dto) {
        Users user = saveUser(dto);
        Mahasiswa mahasiswa = saveMahasiswa(dto, user);
        return mahasiswa;
    }
    // Create User
    private Users saveUser(MahasiswaRequestDto dto){
        Users user = new Users();
        user.setUserName(dto.getUserName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Roles mahasiswaRoles = rolesRepository.findByRoleName(RolesConstant.MAHASISWA_ROLE);
        user.setRoles(mahasiswaRoles);
        return usersRepository.save(user);
    }
    // Create Mahasiswa
    private Mahasiswa saveMahasiswa(MahasiswaRequestDto dto, Users user){
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNim(dto.getNim());
        mahasiswa.setMahasiswaName(dto.getMahasiswaName());
        LocalDate dateOfBirth = LocalDate.parse(dto.getDateOfBirth() , DateTimeFormatter.ISO_DATE);
        mahasiswa.setDateOfBirth(dateOfBirth);
        mahasiswa.setGender(dto.getGender());
        mahasiswa.setRegisterDate(LocalDate.now());
        return mahasiswaRepository.save(mahasiswa);
    }
}
