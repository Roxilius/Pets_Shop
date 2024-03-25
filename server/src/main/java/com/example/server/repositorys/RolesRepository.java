package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.server.models.Roles;

public interface RolesRepository extends JpaRepository<Roles, String>{
    @Query
    Roles findByRoleName(String roleName);
}