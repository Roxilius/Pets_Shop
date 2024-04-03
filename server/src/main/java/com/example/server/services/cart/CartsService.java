package com.example.server.services.cart;

import java.util.List;

import com.example.server.data_transfer_object.user.CartRequest;
import com.example.server.data_transfer_object.user.CartResponse;

public interface CartsService {
    public void add(CartRequest request);
    public List<CartResponse> findAllCart();
    public void delete(String id);

}
