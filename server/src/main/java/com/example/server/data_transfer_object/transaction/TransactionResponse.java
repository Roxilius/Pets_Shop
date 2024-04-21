package com.example.server.data_transfer_object.transaction;

import java.time.LocalDateTime;
import java.util.List;

import com.example.server.data_transfer_object.products.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private LocalDateTime date;
    private Integer totalAmount;
    List<ProductResponse> products;
}
