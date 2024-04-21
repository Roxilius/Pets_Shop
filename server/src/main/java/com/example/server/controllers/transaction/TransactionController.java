package com.example.server.controllers.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.services.transaction.TransactionService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transaction")
@Tag(name = "transaction")
@CrossOrigin(origins = "http://localhost:5173/")
@Slf4j
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @PostMapping("/buy")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> buy(){
        try {
            transactionService.buy();
            return ResponseEntity.ok().body(GenericResponse.success(null, "Successfully Buy Products"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
    @PostMapping("/topup")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> topUp(@RequestParam(value = "amount") Integer amount){
        try {
            transactionService.topUp(amount);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Successfully Top-Up Saldo"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
}
