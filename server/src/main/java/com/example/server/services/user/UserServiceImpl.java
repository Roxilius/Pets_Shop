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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.constants.RolesConstant;
import com.example.server.data_transfer_object.user.ChangePasswordRequest;
import com.example.server.data_transfer_object.user.LoginRequest;
import com.example.server.data_transfer_object.user.LoginResponse;
import com.example.server.data_transfer_object.user.UserRequest;
import com.example.server.data_transfer_object.user.UserResponse;
import com.example.server.jwt.JwtUtil;
import com.example.server.models.ForgotPassword;
import com.example.server.models.Roles;
import com.example.server.models.Users;
import com.example.server.repositorys.ForgotPasswordRepository;
import com.example.server.repositorys.RolesRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.email.EmailService;
import com.example.server.services.image.ImageService;

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
    @Autowired
    ImageService imageService;

    @Override
    @Transactional
    public UserRequest register(UserRequest request) {
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
            emailSevice.emailRegistration(request.getEmail(), request.getFullName().toUpperCase());
            return request;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Sudah Terdaftar");
    }
    public UserRequest editProfile(UserRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());
        Users emailUser = usersRepository.findByEmail(request.getEmail()).orElse(null);
        if (request.getEmail().equals(emailUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Sudah Terdaftar");
        }
        user.setFullName(request.getFullName());
        user.setGender(request.getGender());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        usersRepository.save(user);
        return request;
    }
    @Override
    public UserResponse profile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());
        try {
            return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .dateOfBirth(user.getDateOfBirth())
            .address(user.getAddress())
            .phoneNumber(user.getPhoneNumber())
            .gender(user.getGender())
            .image(user.getImage() != null ? imageService.convertImage(user.getImage()) : null)
            .build();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
    public void uploadUserImage(MultipartFile userImage) throws IOException, SQLException {
        if (!userImage.getContentType().startsWith("image")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported File Type");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());
        user.setImage(new SerialBlob(userImage.getBytes()));
        usersRepository.save(user);
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
