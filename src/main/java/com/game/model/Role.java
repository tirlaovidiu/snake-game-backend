package com.game.model;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 08.04.2018 #
 ******************************
*/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role")
    private String role;

    public Role() {
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return role;
    }

    public void setRoleName(String role) {
        this.role = role;
    }
}
