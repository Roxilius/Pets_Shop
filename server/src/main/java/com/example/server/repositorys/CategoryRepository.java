package com.example.server.repositorys;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{
    Category findCategoryByName(String name);
    Optional<Category> findByName(String name);
}