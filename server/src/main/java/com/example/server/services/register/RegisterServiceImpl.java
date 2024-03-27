package com.example.server.services.register;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.constants.RolesConstant;
import com.example.server.data_transfer_object.register.RegisterRequest;
import com.example.server.models.Roles;
import com.example.server.models.Users;
import com.example.server.repositorys.RolesRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.email.EmailService;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    EmailService emailSevice;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RolesRepository rolesRepository;

    @Override
    @Transactional
    public Users register(RegisterRequest request) {
        Users user = usersRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            Users newUser = new Users();
            newUser.setFullName(request.getFullName());
            newUser.setGender(request.getGender());
            newUser.setPhoneNumber(request.getPhoneNumber());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            Roles userRoles = rolesRepository.findByRoleName(RolesConstant.USER_ROLE);
            newUser.setRoles(userRoles);
            newUser.setRegisterDate(LocalDate.now());

            usersRepository.save(newUser);
            sendEmail(request.getEmail(), request.getFullName().toUpperCase());
            return newUser;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Sudah Terdaftar");
    }

    private void sendEmail(String to, String name) {
        String subject = "Registration New User";
        String text = 
            "Selamat Datang, " + name + "\n" +
            "Kepada Yth " + name + ", Terimakasih sudah mendaftar di Website kami. \n" + 
            "Email ini akan bermanfaat untuk membantu Anda menggunakan akun diwebsite kami dengan mudah. \n" +
            "Silahkan login melalui link berikut: (link)";
        emailSevice.sendSimpleMessage(to, subject, text);
    }
}
