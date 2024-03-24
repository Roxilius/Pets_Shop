package com.example.server.initial;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.server.constants.RolesConstant;
import com.example.server.models.Roles;
import com.example.server.repositorys.RolesRepository;

@Component
public class InitialDataLoader implements ApplicationRunner{
    @Autowired
    RolesRepository rolesRepository;

    @SuppressWarnings("null")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Roles> roles = rolesRepository.findAll();
        if(roles.isEmpty()){
            Roles dosen = new Roles(null, RolesConstant.DOSEN_ROLE, "Role as Dosen in Application");
            Roles mahasiswa = new Roles(null, RolesConstant.MAHASISWA_ROLE, "Role as Mahasiswa in Application");
            Roles admin = new Roles(null, RolesConstant.AKADEMIK_ROLE, "Role as Admin in Application");
            
            rolesRepository.saveAll(List.of(dosen, mahasiswa, admin));
        }
    }
}
