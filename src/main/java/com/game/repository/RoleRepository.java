package com.game.repository;

import com.game.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 08.04.2018 #
 ******************************
*/
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
