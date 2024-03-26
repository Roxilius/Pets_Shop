package com.example.server.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.auth.LoginRequest;
import com.example.server.data_transfer_object.auth.LoginResponse;
import com.example.server.jwt.JwtUtil;
import com.example.server.models.Users;
import com.example.server.repositorys.UsersRepository;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    UsersRepository usersRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;
    @Override
    public LoginResponse login(LoginRequest request) {

        Users user = usersRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            Boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
            if (isMatch) {
                LoginResponse response = new LoginResponse();
                response.setUserName(user.getEmail());
                response.setRole(user.getRoles().getRoleName());
                response.setToken(jwtUtil.generateToken(user));
                return response;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid usename or password");
    }
}
