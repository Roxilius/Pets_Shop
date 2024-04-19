package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Cart;
import com.example.server.models.CartItems;
import com.example.server.models.Users;


public interface CartRepository extends JpaRepository<Cart, String>{
    Cart findCartByUsers(Users user);
    Cart findCartByCartItems(CartItems cartItems);
}
