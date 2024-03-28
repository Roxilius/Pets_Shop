package com.example.server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.models.Roles;

public interface RolesRepository extends JpaRepository<Roles, String>{
    Roles findByRoleName(String roleName);
}