package com.game.controller;

import com.game.service.RoleService;
import com.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 08.04.2018 #
 ******************************
*/
@Controller
public class HelloController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public HelloController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        return "Hi from spring";
    }
}
