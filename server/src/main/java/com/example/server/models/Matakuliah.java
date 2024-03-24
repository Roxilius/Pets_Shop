package com.example.server.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matakuliah {
    @Id
    @Column(name = "id", length = 6, nullable=false)
    private String idMatkul;

    @Column(name = "nama_matkul", length = 36, nullable=false)
    private String namaMatkul;
    
    @Column(name = "sks", length = 1, nullable=false)
    private Integer sks;

    
}
