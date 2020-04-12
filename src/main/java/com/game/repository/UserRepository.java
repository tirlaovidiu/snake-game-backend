package com.game.repository;

import com.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 08.04.2018 #
 ******************************
*/
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
}
