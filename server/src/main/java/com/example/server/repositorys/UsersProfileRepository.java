package com.example.server.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.UsersProfile;

public interface UsersProfileRepository extends JpaRepository<UsersProfile, String>{
    Optional<UsersProfile> findByEmail(String email);
}
