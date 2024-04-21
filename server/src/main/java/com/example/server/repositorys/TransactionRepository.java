package com.example.server.repositorys;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.server.models.Transaction;
import com.example.server.models.Users;

public interface TransactionRepository extends JpaRepository<Transaction, String>{
    @Query("select t from Transaction t where t.users = :user and t.date between :startDate and :endDate")
    List<Transaction> daily(Users user, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select t from Transaction t where t.users = :user and Month(t.date) = :month")
    List<Transaction> monthly(Users user, String month);

    @Query("select t from Transaction t where t.users = :user and Year(t.date) = :year")
    List<Transaction> annual(Users user, String year);

    @Query("select t from Transaction t where t.users = :user and t.date between :startDate and :endDate")
    public List<Transaction> expenses(Users user, LocalDateTime startDate, LocalDateTime endDate);
}
