package com.example.server.services.register;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.server.constants.RolesConstant;
import com.example.server.data_transfer_object.user.UsersRegistrationRequestDto;
import com.example.server.models.Roles;
import com.example.server.models.Users;
import com.example.server.models.UsersProfile;
import com.example.server.repositorys.RolesRepository;
import com.example.server.repositorys.UsersProfileRepository;
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
    UsersProfileRepository usersProfileRepository;

    @Transactional
    @Override
    public UsersProfile register(UsersRegistrationRequestDto dto){
        Users user = saveUser(dto);
        UsersProfile profile = saveProfile(dto, user);
        return profile;
    }

    private Users saveUser(UsersRegistrationRequestDto dto){
        Users user = new Users();
        user.setUserName(dto.getEmail());
        user.setPassword(dto.getPassword());
        Roles userRoles = rolesRepository.findByRoleName(RolesConstant.USER_ROLE);
        user.setRoles(userRoles);
        return usersRepository.save(user);
    }

    private UsersProfile saveProfile(UsersRegistrationRequestDto dto, Users user){
        UsersProfile profile = new UsersProfile();
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        LocalDate dateOfBirth = LocalDate.parse(dto.getDateOfBirth(),
        DateTimeFormatter.ISO_DATE);
        profile.setDateOfBirth(dateOfBirth);
        profile.setAddress(dto.getAddress());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setGender(dto.getGender());
        profile.setRegisterDate(LocalDate.now());
        return usersProfileRepository.save(profile);
    }
}
