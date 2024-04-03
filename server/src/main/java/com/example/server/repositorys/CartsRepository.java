package com.example.server.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Carts;
import com.example.server.models.Products;
import com.example.server.models.Users;

public interface CartsRepository extends JpaRepository<Carts, String>{
    List<Carts> findByUsers(Users user);
    Carts findByUsersAndProducts(Users user,Products product);
}
