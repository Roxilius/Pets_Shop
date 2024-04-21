package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Topup;

public interface TopupRepository extends JpaRepository<Topup, String>{
    
}
