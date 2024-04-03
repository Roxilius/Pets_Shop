package com.example.server.controllers.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.data_transfer_object.user.CartRequest;
import com.example.server.services.cart.CartsService;

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
    CartsService cartsService;

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> findAll(){
        try {
            return ResponseEntity.ok().body(GenericResponse.success(cartsService.findAllCart(), "Successfully Get All Cart"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addCart(@RequestBody CartRequest request){
        try {
            cartsService.add(request);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Successfully Add Cart"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> remove(@PathVariable String id){
        try {
            cartsService.delete(id);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Successfully Delete cart"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
}