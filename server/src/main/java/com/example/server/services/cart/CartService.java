package com.example.server.services.cart;

import com.example.server.data_transfer_object.user.CartRequest;
import com.example.server.data_transfer_object.user.CartResponse;

public interface CartService {
    void add(CartRequest request);
    CartResponse findAllCart();
    void deleteAll();
    void deleteCartItems(String id);
}
