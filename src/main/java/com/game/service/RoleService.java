package com.game.service;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 08.04.2018 #
 ******************************
*/

import com.game.model.Role;
import com.game.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> findAll() {
        return repository.findAll();
    }

    public Role getRoleByName(String roleName) {
        List<Role> roles = repository.findAll();
        for (Role rol : roles) {
            if (rol.getRoleName().equals(roleName))
                return rol;
        }
        return null;
    }
}
