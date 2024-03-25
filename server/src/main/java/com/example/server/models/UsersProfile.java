package com.example.server.models;

import java.sql.Blob;
import java.time.LocalDate;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="profile")
@NoArgsConstructor
@AllArgsConstructor
public class UsersProfile {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable=false)
    private String id;

    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "email")
    private String email;

    @Column(name = "dat_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", length = 2000)
    private String address;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "gender", length = 25)
    private String gender;

    @Lob
    @Column(name = "photo")
    private Blob photo;

    @Column(name = "register_date")
    private LocalDate registerDate;

        
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users user;
}
