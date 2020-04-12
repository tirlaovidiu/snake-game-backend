package com.game.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 08.04.2018 #
 ******************************
*/
@RestController
public class LoginController {
    @RequestMapping(value = "/errorLogin")
    public ResponseEntity failure(HttpServletRequest request) {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/successLogin")
    public ResponseEntity success(HttpServletRequest request) {
        return new ResponseEntity(HttpStatus.OK);
    }
}
