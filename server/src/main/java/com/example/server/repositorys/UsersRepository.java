package com.example.server.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;

import com.example.server.models.Users;

import jakarta.transaction.Transactional;

// import jakarta.transaction.Transactional;

public interface UsersRepository extends JpaRepository<Users, String>{
    Optional<Users> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Users u set u.password = :password where u.email = :email")
    void updatePassword(String password, String email);
}
