package com.example.server.controllers.transaction;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/products")
@Tag(name = "transaction")
@CrossOrigin(origins = "http://localhost:5173/")
public class TransactionController {
    
}
