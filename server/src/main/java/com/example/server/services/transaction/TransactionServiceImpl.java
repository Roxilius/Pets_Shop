package com.example.server.services.transaction;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.models.Transaction;
import com.example.server.repositorys.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void buy() {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.now());
    }
}