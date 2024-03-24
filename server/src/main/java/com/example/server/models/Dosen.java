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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dosen {
    @Id
    @UuidGenerator
    @Column(name ="id", length = 36, nullable=false)
    private String id;

    @Column(name = "nip", length=25)
    private String nip;

    @Column(name = "nama_dosen")
    private String teacherName;

    @Column(name = "dat_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth", length = 100)
    private String placeOfBirth;
    
    @Column(name = "gender", length = 25)
    private String gender;

    @Column(name = "address", length = 2000)
    private String address;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "photo")
    private Blob photo;

    @OneToOne
    @JoinColumn(name="users_id", referencedColumnName = "id", nullable = false)
    private Users users;
}
