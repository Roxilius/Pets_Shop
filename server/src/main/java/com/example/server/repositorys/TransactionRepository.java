package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{
    
}
