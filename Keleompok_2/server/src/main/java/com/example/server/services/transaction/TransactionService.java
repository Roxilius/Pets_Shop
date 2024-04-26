package com.example.server.services.transaction;

import com.example.server.data_transfer_object.transaction.TransactionResponse;

public interface TransactionService {
    TransactionResponse buy();
    void topUp(Integer amount);
}
