package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.CartItems;

public interface CartItemsRepository extends JpaRepository<CartItems, String>{
    
}
