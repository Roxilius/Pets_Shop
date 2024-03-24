package com.example.server.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Users;

public interface UsersRepository extends JpaRepository<Users,String>{
    Optional<Users> findByUserName(String username);
}
