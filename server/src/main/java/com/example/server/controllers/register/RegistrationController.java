package com.example.server.controllers.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.register.GenericResponse;
import com.example.server.data_transfer_object.register.RegisterRequest;
import com.example.server.models.Users;
import com.example.server.services.register.RegisterService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name= "user")
public class RegistrationController {
    @Autowired
    RegisterService registerService;
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request){
        try{
            Users response = registerService.register(request);
            return ResponseEntity.ok().body(GenericResponse.success(response,
            "Successfully Register New User"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }
}
