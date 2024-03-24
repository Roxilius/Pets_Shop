package com.example.server.models;

import java.sql.Blob;
import java.time.LocalDate;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mahasiswa {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable=false)
    private String id;

    @Column(name = "nim", length = 11)
    private String nim;

    @Column(name = "nama_mahasiswa")
    private String mahasiswaName;

    @Column(name = "dat_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth", length = 100)
    private String placeOfBirth;

    @Column(name = "gender", length = 25)
    private String gender;

    @Lob
    @Column(name = "photo")
    private Blob photo;

    @Column(name = "register_date")
    private LocalDate registerDate;

    
}