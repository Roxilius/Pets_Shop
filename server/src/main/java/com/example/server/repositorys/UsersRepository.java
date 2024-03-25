package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Users;

public interface UsersRepository extends JpaRepository<Users, String>{
    String findByUserName(String username);
}
