package com.example.server.data_transfer_object.user;

import java.util.List;

import com.example.server.data_transfer_object.products.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private String id;
    private int quantity;
    private String username;
    List<ProductResponse> product;
}
