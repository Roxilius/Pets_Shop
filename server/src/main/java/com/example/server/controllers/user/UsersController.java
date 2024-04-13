package com.example.server.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.data_transfer_object.user.ChangePasswordRequest;
import com.example.server.data_transfer_object.user.LoginRequest;
import com.example.server.data_transfer_object.user.LoginResponse;
import com.example.server.data_transfer_object.user.UserRequest;
import com.example.server.services.user.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name = "user")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173/")
public class UsersController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserRequest request){
        try{
            return ResponseEntity.ok().body(GenericResponse.success(userService.register(request),
            "Successfully Register New User"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
    @PutMapping("auth/edit-profile")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> editProfile(@RequestBody UserRequest request){
        try{
            return ResponseEntity.ok().body(GenericResponse.success(userService.editProfile(request),
            "Successfully Edit Profile"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @GetMapping("auth/profile")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> profile(){
        try{
            return ResponseEntity.ok().body(GenericResponse.success(userService.profile(),
            "Successfully Get Profile"));
        } catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @PostMapping(value="/upload-user-image",
    consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> uploadUserImage(@RequestParam("UserImage") MultipartFile file){
        try{
            userService.uploadUserImage(file);
            return ResponseEntity.ok().body(GenericResponse.success(null,"Successfuly Upload Image"));
        }catch(ResponseStatusException e){
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Image Upload Failed"));
        }
    }
    @PostMapping("auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request){
        try{
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok().body(GenericResponse.success(response,
            "Successfully login"));
        }catch(ResponseStatusException e){
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @PostMapping("auth/verify-email/{email}")
    public ResponseEntity<Object> verifyEmail(@PathVariable String email){
        try {
            userService.verifyEmail(email);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Cek Your Email For OTP Verification"));
        }catch(ResponseStatusException e){
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
    
    @PostMapping("auth/verify-otp/{otp}/{email}")
    public ResponseEntity<Object> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        try {
            userService.verifyOtp(otp, email);
            return ResponseEntity.ok().body(GenericResponse.success(null, "OTP Verified!!"));
        }catch(ResponseStatusException e){
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @PostMapping("auth/change-password/{email}")
    public ResponseEntity<Object> changePasswordHandler(@RequestBody ChangePasswordRequest request, @PathVariable String email){
        try {
            userService.changePasswordHandler(request, email);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Password has be Changed!"));
        }catch(ResponseStatusException e){
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
}
