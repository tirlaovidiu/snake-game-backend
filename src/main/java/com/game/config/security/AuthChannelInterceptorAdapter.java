package com.game.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.03.2018 #
 ******************************
*/
@Controller
public class AuthChannelInterceptorAdapter extends ChannelInterceptorAdapter {
    private final WebSocketAuthenticationService webSocketAuthenticatorService;

    @Autowired
    public AuthChannelInterceptorAdapter(final WebSocketAuthenticationService webSocketAuthenticatorService) {
        this.webSocketAuthenticatorService = webSocketAuthenticatorService;
    }

    @Override
    public Message<?> preSend(final Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT == accessor.getCommand()) {
            final String username = accessor.getLogin();
            final String password = accessor.getPasscode();

            try {
                UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAuthenticatedOrFail(username, password);
                accessor.setUser(user);
            } catch (Exception e) {
                throw new IllegalArgumentException("No permission for this !");
            }

        }

        return message;
    }
}