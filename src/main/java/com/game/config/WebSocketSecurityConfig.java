package com.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.03.2018 #
 ******************************
*/
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.HEARTBEAT, SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT).permitAll()
//                .simpSubscribeDestMatchers("/user/queue/error").permitAll()
                .anyMessage().authenticated();

//        // message types other than MESSAGE and SUBSCRIBE
//        messages
//
//                // matches any destination that starts with /rooms/
//                .simpDestMatchers("/topic/**").authenticated()
////                .simpDestMatchers("/app/**").authenticated()
//                // (i.e. cannot send messages directly to /topic/, /queue/)
//                // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
//                // /topic/messages-user<id>)
//                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
//                // catch all
//                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        //TODO Fix CSRF
        return true;
    }
}
