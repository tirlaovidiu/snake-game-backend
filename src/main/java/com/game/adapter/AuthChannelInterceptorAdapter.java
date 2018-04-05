package com.game.adapter;

import com.game.service.WebSocketAuthenticationService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.03.2018 #
 ******************************
*/
public class AuthChannelInterceptorAdapter extends ChannelInterceptorAdapter {

    private final WebSocketAuthenticationService webSocketAuthenticatorService;

    public AuthChannelInterceptorAdapter(final WebSocketAuthenticationService webSocketAuthenticatorService) {
        this.webSocketAuthenticatorService = webSocketAuthenticatorService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            final String username = accessor.getLogin();
            final String password = accessor.getPasscode();

            UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAuthenticatedOrFail(username, password);
            accessor.setUser(user);

        }

        return message;
    }

}
