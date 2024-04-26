package com.example.server.data_transfer_object.user;

import java.util.List;


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
    private String email;
    private Integer totalAmount;
    private List<CartItemsResponse> cartItems;
}
