package com.example.server.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.auth.LoginRequestDto;
import com.example.server.data_transfer_object.auth.LoginResponseDto;
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
    public LoginResponseDto login(LoginRequestDto dto) {
        Users user = usersRepository.findByUserName(dto.getUserName()).orElse(null);
        if (user != null) {
            Boolean isMatch = passwordEncoder.matches(dto.getPassword(), user.getPassword());
            if (isMatch) {
                LoginResponseDto response = new LoginResponseDto();
                response.setUserName(user.getUserName());
                response.setRole(user.getRoles().getRoleName());
                response.setToken(jwtUtil.generateToken(user));
                return response;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid usename or password");
    }
}
