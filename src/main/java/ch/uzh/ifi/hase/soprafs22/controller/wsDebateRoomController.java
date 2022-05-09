package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.WSMsg;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class wsDebateRoomController {

    private final Logger log = LoggerFactory.getLogger(DebateService.class);

    ///current post debates/rooms/{roomId}/msg
    // Handles messages from /debates/rooms/{roomId}/msg. (The Spring adds the /debates prefix for us).
    // Need to send to this url
    @MessageMapping("/debates/rooms/{roomId}/msg")
    // Sends the return value of this method to /debateRoom/{roomId}
    // Need to subscribe to this url
    @SendTo("/debates/rooms/{roomId}")
    public WSMsg getMessages(@DestinationVariable Long roomId, @Payload WSMsg msg) {

        if (!Objects.isNull(msg.getDebateState())){
            System.out.println(msg.getDebateState());
        }

        return msg;
    }


}
