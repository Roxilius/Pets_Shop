package com.example.server.models;

import java.util.Date;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPassword {
    @Id
    @UuidGenerator
    @Column(name ="id", length = 36, nullable=false)
    private String id;
    
    @Column(nullable = false)
    private Integer otp;
    
    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private Users user;
}
