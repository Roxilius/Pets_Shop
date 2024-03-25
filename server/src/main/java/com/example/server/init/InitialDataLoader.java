package com.example.server.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.example.server.constants.RolesConstant;
import com.example.server.models.Roles;
import com.example.server.repositorys.RolesRepository;

public class InitialDataLoader implements ApplicationRunner{
    @Autowired
    RolesRepository rolesRepository;

    @SuppressWarnings("null")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Roles> roles = rolesRepository.findAll();

        if(roles.isEmpty()){
            Roles admin = new Roles(null, RolesConstant.ADMIN_ROLE, "Role as Admin in Application");
            Roles user = new Roles(null, RolesConstant.USER_ROLE, "Role as User in Application");
            rolesRepository.saveAll(List.of(admin, user));
        }
    }
}
