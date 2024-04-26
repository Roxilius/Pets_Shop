package com.example.server.data_transfer_object.user;


import com.example.server.data_transfer_object.products.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemsResponse {
    private String id;
    private Integer qty;
    private Integer amount;
    private ProductResponse product;
}
