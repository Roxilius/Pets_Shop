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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.constants.RolesConstant;
import com.example.server.data_transfer_object.user.ChangePasswordRequest;
import com.example.server.data_transfer_object.user.LoginRequest;
import com.example.server.data_transfer_object.user.LoginResponse;
import com.example.server.data_transfer_object.user.RegisterRequest;
import com.example.server.models.ForgotPassword;
import com.example.server.models.Roles;
import com.example.server.models.Users;
import com.example.server.repositorys.ForgotPasswordRepository;
import com.example.server.repositorys.RolesRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.security.jwt.JwtUtil;
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
    // overloading
    private void sendEmail(String to, String name) {
        String subject = "Registration New User";
        String text = 
            "Selamat Datang, " + name + "\n" +
            "Kepada Yth " + name + ", Terimakasih sudah mendaftar di Website kami. \n" + 
            "Email ini akan bermanfaat untuk membantu Anda menggunakan akun diwebsite kami dengan mudah. \n" +
            "Silahkan login melalui link berikut: (link)";
        emailSevice.sendSimpleMessage(to, subject, text);
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
    public void verifyEmail(@PathVariable String email){
        Users user = usersRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Email Tidak Ditemukan!!"));
        
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
        sendEmail(email, otp);
        forgotPasswordRepository.save(fp);
    }
    // overloading
    private void sendEmail(String to, int otp) {
        String subject = "Verify Forgot Password";
        String text = "OTP Code For Forgot Your Password : " + otp;
        emailSevice.sendSimpleMessage(to, subject, text);
    }
    @SuppressWarnings("null")
    @Override
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        Users user = usersRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Email Not Found!!"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
        .orElseThrow(() -> new UsernameNotFoundException("Invalid OTP for Email : " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getId());
            return new ResponseEntity<>("OTP has Expired", HttpStatus.EXPECTATION_FAILED);
        }
        forgotPasswordRepository.deleteById(fp.getId());
        return ResponseEntity.ok("OTP Verified!!");
    }
    @Override
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest request, @PathVariable String email){
        if (!Objects.equals(request.getPassword(), request.getRePassword())) {
            return new ResponseEntity<>("Please Enter the Password Again", HttpStatus.EXPECTATION_FAILED);
        }
        String password = passwordEncoder.encode(request.getPassword());
        usersRepository.updatePassword(password, email);
        return ResponseEntity.ok("Password has be Changed!");
    }
}
