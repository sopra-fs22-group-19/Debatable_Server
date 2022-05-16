package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Intervention;
import ch.uzh.ifi.hase.soprafs22.entity.WSMsg;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WSDebateRoomController {


    private final DebateService debateService;

    public WSDebateRoomController(DebateService debateService) {
        this.debateService = debateService;
    }

    ///current post debates/rooms/{roomId}/msg
    // Handles messages from /debates/rooms/{roomId}/msg. (The Spring adds the /debates prefix for us).
    // Need to send to this url
    @MessageMapping("/debates/rooms/{roomId}/msg")
    // Sends the return value of this method to /debateRoom/{roomId}
    // Need to subscribe to this url
    @SendTo("/debates/rooms/{roomId}")
    public WSMsg getMessages(@DestinationVariable Long roomId, @Payload WSMsg msg) {

        if (msg.getMessageContent() != null) {
            System.out.println("outside loop");
            System.out.println(msg.getMessageContent());
            if (!msg.getMessageContent().isEmpty() && !msg.getMessageContent().trim().isEmpty()) {
                System.out.println("inside loop");
                System.out.println(msg.getMessageContent());
                // Store messages in DB
                Intervention intervention = new Intervention();
                intervention.setMessage(msg.getMessageContent());
                debateService.createIntervention(intervention, roomId, msg.getUserId());
            }
        }

        return msg;
    }


}
