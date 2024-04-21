package com.example.server.repositorys;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.server.models.Topup;
import com.example.server.models.Users;

public interface TopupRepository extends JpaRepository<Topup, String>{
    @Query("select t from Topup t where t.users = :user and t.topupDate between :startDate and :endDate")
    List<Topup> daily(Users user, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select t from Topup t where t.users = :user and Month(t.topupDate) = :month")
    List<Topup> monthly(Users user, String month);

    @Query("select t from Topup t where t.users = :user and Year(t.topupDate) = :year")
    List<Topup> annual(Users user, String year);

    @Query("select t from Topup t where t.users = :user and t.topupDate between :startDate and :endDate")
    public List<Topup> income(Users user, LocalDateTime startDate, LocalDateTime endDate);
}
