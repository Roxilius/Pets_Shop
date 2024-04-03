package com.example.server.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Products;

public interface ProductsRepository extends JpaRepository<Products, String>{
    Optional<Products> findByName(String name);
    Products findProductsById(String id);
}
