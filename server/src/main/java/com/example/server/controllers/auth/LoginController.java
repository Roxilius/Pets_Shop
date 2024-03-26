package com.example.server.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.data_transfer_object.auth.LoginRequest;
import com.example.server.data_transfer_object.auth.LoginResponse;
import com.example.server.services.auth.LoginService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/auth")
@RestController
@Tag(name = "user")
public class LoginController {
    @Autowired
    LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request){
        try{
            LoginResponse response = loginService.login(request);
            return ResponseEntity.ok().body(GenericResponse.success(response,
            "Successfully login"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }
}
