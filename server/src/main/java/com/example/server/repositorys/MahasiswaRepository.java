package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Mahasiswa;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, String>{
    
}