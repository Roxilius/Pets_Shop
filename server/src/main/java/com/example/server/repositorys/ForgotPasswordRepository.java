package com.example.server.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.server.models.ForgotPassword;
import com.example.server.models.Users;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, String >{
    @Query("select fp from ForgotPassword fp where fp.otp = :otp and fp.user = :user")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, Users user);
}
