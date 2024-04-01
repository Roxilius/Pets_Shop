package com.example.server.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.server.constants.RolesConstant;
import com.example.server.models.Roles;
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

    @SuppressWarnings("null")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Roles> roles = rolesRepository.findAll();

        if(roles.isEmpty()){
            Roles admin = new Roles(null, RolesConstant.ADMIN_ROLE, "Role as Admin in Application");
            Roles user = new Roles(null, RolesConstant.USER_ROLE, "Role as User in Application");
            rolesRepository.saveAll(List.of(admin, user));
        }

        // Roles adminRoles = rolesRepository.findByRoleName(RolesConstant.USER_ROLE);
        // Users user = Users.builder()
        // .email("fajrikhairan08@gmail.com")
        // .fullName("fajri khairan")
        // .gender("laki - laki")
        // .phoneNumber("085161501710")
        // .password(passwordEncoder.encode("fajri"))
        // .roles(adminRoles)
        // .build();
        // usersRepository.saveAndFlush(user);
    }
}
