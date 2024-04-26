package com.example.server.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.server.constants.RolesConstant;
import com.example.server.models.Category;
import com.example.server.models.Roles;
import com.example.server.models.Users;
import com.example.server.repositorys.CategoryRepository;
// import com.example.server.models.Users;
import com.example.server.repositorys.RolesRepository;
import com.example.server.repositorys.UsersRepository;

@Component
public class InitialDataLoader implements ApplicationRunner{
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(rolesRepository.findAll().isEmpty()){
            Roles admin = new Roles(null, RolesConstant.ADMIN_ROLE, "Role as Admin in Application");
            Roles user = new Roles(null, RolesConstant.USER_ROLE, "Role as User in Application");
            rolesRepository.saveAll(List.of(admin, user));
        }

        if(usersRepository.findAll().isEmpty()){
            Roles adminRoles = rolesRepository.findByRoleName(RolesConstant.ADMIN_ROLE);
            Users user = Users.builder()
            .email("petshop")
            .password(passwordEncoder.encode("petshop"))
            .roles(adminRoles)
            .build();
            usersRepository.saveAndFlush(user);
        }

        if (categoryRepository.findAll().isEmpty()) {
            Category category = new Category();
            category.setName("Makanan");
            categoryRepository.save(category);

            category = new Category();
            category.setName("Obat - Obatan");
            categoryRepository.save(category);

            category = new Category();
            category.setName("Perlengkapan");
            categoryRepository.save(category);
        }
    }
}
