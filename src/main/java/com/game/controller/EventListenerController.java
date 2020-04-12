package com.game.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 02.04.2018 #
 ******************************
*/
@Controller
public class EventListenerController {

    private static final Logger log = LoggerFactory.getLogger(EventListenerController.class);

    private static final String CONNECTED = "[CONNECTED]";
    private static final String SUBSCRIBED = "[SUBSCRIBED]";
    private static final String UNSUBSCRIBE = "[UN-SUBSCRIBE]";
    private static final String DISCONNECTED = "[DISCONNECTED]";
    private static final String SNAKEGAME = "[SNAKE-GAME]";

    private final GameController gameController;
    private final SimpMessagingTemplate template;

    private static final String SERVER = "SERVER";

    private SessionRegistryImpl sessionRegistry;

    @Autowired
    public EventListenerController(GameController gameController, SimpMessagingTemplate template) {
        this.gameController = gameController;
        this.template = template;
    }

    @EventListener
    public void onSocketConnected(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        if (sha.getUser() == null) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
            headerAccessor.setMessage(String.valueOf(event.getMessage()));
            headerAccessor.setSessionId(sha.getSessionId());
            template.send(sha.getSessionId(), MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
            return;
        }
        log.debug(CONNECTED + sha.getSessionId() + " " + event.getUser().getName());
//        System.out.println("[Connected] " + event.getMessage().getHeaders());
//        System.out.println("[Connected] " + Arrays.toString(event.getMessage().getPayload()));
//        template.convertAndSend(sha.getSessionId(), "test");
//        template.convertAndSend(sha.getDestination(), new SnakeMessage(SERVER, "WAIT FOR PLAYERS", "lobby1", EventType.HELLO_EVENT));
    }

    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.debug(DISCONNECTED + sha.getSessionId());
        gameController.removeIfWasInGame(sha.getSessionId());
        log.debug(SNAKEGAME + gameController.getRunningGamesCounter());
//        sessionRegistry.getAllPrincipals()
//        sessionRegistry.getSessionInformation(sha.getSessionId()).expireNow();


    }

    @EventListener
    public void onTopicSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sha.getSessionId());
        headerAccessor.setLeaveMutable(true);

        log.debug(SUBSCRIBED + sha.getDestination() + " - " + sha.getSessionId());

//        template.convertAndSendToUser(sha.getSessionId(), "/queue/reply", new OutputMessage(SERVER, "LOGGED", "TOPIC"), headerAccessor.getMessageHeaders());

//        System.out.println("[Subscribe] " + event.getUser());
//        System.out.println("[Subscribe] " + event.getMessage());
    }

    @EventListener
    public void onTopicUnSubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.debug(UNSUBSCRIBE + sha.getDestination() + " - " + sha.getSessionId());
    }

    @MessageMapping("/hello")
    public void hello(Message message, Principal principal) {
        System.out.println(message.getPayload().toString());
        System.out.println(principal.toString());
    }
}
