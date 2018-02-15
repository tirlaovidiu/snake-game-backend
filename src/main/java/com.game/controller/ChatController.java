package com.game.controller;

import com.game.util.Message;
import com.game.util.OutputMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 16.02.2018 #
 ******************************
*/
@Controller
public class ChatController {
    @MessageMapping("/chat/{topic}")
    @SendTo("/topic/messages")
    public OutputMessage send(@DestinationVariable("topic") String topic,
                              Message message) {
        return new OutputMessage(message.getFrom(), message.getText(), topic);
    }
}
