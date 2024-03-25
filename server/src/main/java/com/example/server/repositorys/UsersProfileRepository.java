package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.UsersProfile;

public interface UsersProfileRepository extends JpaRepository<UsersProfile, String>{
    String findByEmail(String email);
}
