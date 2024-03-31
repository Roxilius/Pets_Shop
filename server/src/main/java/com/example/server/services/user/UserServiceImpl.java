package com.example.server.services.user;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.constants.RolesConstant;
import com.example.server.data_transfer_object.user.ChangePasswordRequest;
import com.example.server.data_transfer_object.user.LoginRequest;
import com.example.server.data_transfer_object.user.LoginResponse;
import com.example.server.data_transfer_object.user.Register;
import com.example.server.jwt.JwtUtil;
import com.example.server.models.ForgotPassword;
import com.example.server.models.Roles;
import com.example.server.models.Users;
import com.example.server.repositorys.ForgotPasswordRepository;
import com.example.server.repositorys.RolesRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.email.EmailService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    ForgotPasswordRepository forgotPasswordRepository;
    @Autowired
    EmailService emailSevice;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    JwtUtil jwtUtil;

    @Override
    @Transactional
    public Register register(Register request) {
        Users user = usersRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            Users newUser = new Users();
            newUser.setFullName(request.getFullName());
            newUser.setGender(request.getGender());
            newUser.setPhoneNumber(request.getPhoneNumber());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            Roles userRoles = rolesRepository.findByRoleName(RolesConstant.ADMIN_ROLE);
            newUser.setRoles(userRoles);
            newUser.setRegisterDate(LocalDate.now());

            usersRepository.save(newUser);
            emailSevice.emailRegistration(request.getEmail(), request.getFullName().toUpperCase());
            return request;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Sudah Terdaftar");
    }

    @SuppressWarnings("null")
	@Override
    public void uploadUserImage(String userId, MultipartFile userImage) throws IOException, SQLException {
        if (!userImage.getContentType().startsWith("image")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported File Type");
        }
        Users user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setImage(new SerialBlob(userImage.getBytes()));
            usersRepository.saveAndFlush(user);
        }
    }
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
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Username or Password");
    }
    @SuppressWarnings("null")
    @Override
    public void verifyEmail(String email){
        Users user = usersRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email Tidak Ditemukan!!"));
        
        int otp = new Random().nextInt(100_000, 999_999);
        ForgotPassword fp = ForgotPassword.builder()
        .otp(otp)
        .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
        .user(user)
        .build();

        ForgotPassword existFp = forgotPasswordRepository.findByUser(user).orElse(null);
        if (existFp != null ) {
            forgotPasswordRepository.delete(existFp);
        }
        // sendEmail(email, otp);
        forgotPasswordRepository.save(fp);
    }
    @SuppressWarnings("null")
    @Override
    public void verifyOtp(Integer otp, String email){
        Users user = usersRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email Not Found!!"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid OTP for Email : " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"OTP has Expired");
        }
        forgotPasswordRepository.deleteById(fp.getId());
    }
    @Override
    public void changePasswordHandler(ChangePasswordRequest request,String email){
        if (usersRepository.findByEmail(email).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email Not Found!!");
        } else if (!Objects.equals(request.getPassword(), request.getRePassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Please Enter the Password Again");
        }
        String password = passwordEncoder.encode(request.getPassword());
        usersRepository.updatePassword(password, email);
    }
}
