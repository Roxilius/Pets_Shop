package com.example.server.controllers.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.data_transfer_object.user.CartRequest;
import com.example.server.services.cart.CartService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173/")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> findAll(){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(cartService.findAllCart(), "Successfully Get All Cart"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addCart(@RequestBody CartRequest request){
        try {
            cartService.add(request);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Successfully Add Cart"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @DeleteMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> removeAll(){
        try {
            cartService.deleteAll();
            return ResponseEntity.ok().body(GenericResponse.success(null, "Successfully Delete cart"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
}