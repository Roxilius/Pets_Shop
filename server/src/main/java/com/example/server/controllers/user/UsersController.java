package com.example.server.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.data_transfer_object.user.ChangePasswordRequest;
import com.example.server.data_transfer_object.user.LoginRequest;
import com.example.server.data_transfer_object.user.LoginResponse;
import com.example.server.data_transfer_object.user.RegisterRequest;
import com.example.server.models.Users;
import com.example.server.services.user.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name = "user")
@Slf4j
public class UsersController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request){
        try{
            Users response = userService.register(request);
            return ResponseEntity.ok().body(GenericResponse.success(response,
            "Successfully Register New User"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }

    @PostMapping(value="/upload-user-image",
    consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadUserImage(@RequestParam String User, @RequestParam("UserImage") MultipartFile file){
        try{
            userService.uploadUserImage(User, file);
            return ResponseEntity.ok().body(GenericResponse.success(null,"Successfuly Upload Image"));
        }catch(ResponseStatusException e){
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Image Upload Failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request){
        try{
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok().body(GenericResponse.success(response,
            "Successfully login"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }

       @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        try {
            userService.verifyEmail(email);
            return ResponseEntity.ok("Cek Your Email For OTP Verification");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
        
    @PostMapping("/verify-otp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        try {
            userService.verifyOtp(otp, email);
            return ResponseEntity.ok("OTP Verified!!");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change-password/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest request, @PathVariable String email){
        try {
            userService.changePasswordHandler(request, email);
            return ResponseEntity.ok("Password has be Changed!");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
