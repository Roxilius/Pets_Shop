package com.example.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.data_transfer_object.user.UsersRegistrationRequestDto;
import com.example.server.models.UsersProfile;
import com.example.server.services.register.RegistrationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class RegistrationController {
    @Autowired
    RegistrationService registrationService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UsersRegistrationRequestDto dto){
        try {
            UsersProfile profile = registrationService.register(dto);
            return ResponseEntity.ok().body(GenericResponse.success(profile, "Successfully registrasi new student"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
            .body(GenericResponse.eror(e.getMessage()));
        }
    }
}
