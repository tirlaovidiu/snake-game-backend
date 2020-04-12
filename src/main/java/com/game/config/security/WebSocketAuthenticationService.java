package com.game.config.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.03.2018 #
 ******************************
*/
@Component
public class WebSocketAuthenticationService {
    UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String username, final String password) throws AuthenticationException {
        if ((username == null) || username.trim().length() == 0) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if ((password == null) || password.trim().length() == 0) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }

        if (!checkUser(username, password)) {
//            return new UsernamePasswordAuthenticationToken(username, null, Collections.singleton((GrantedAuthority) () -> "GUEST"));
            throw new BadCredentialsException("Bad credentials for user " + username);
        }

        // null credentials, we do not pass the password along to prevent security flaw
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER")
        );
    }

    private boolean checkUser(String username, String password) {
        return password.equals("admin");
    }
}

