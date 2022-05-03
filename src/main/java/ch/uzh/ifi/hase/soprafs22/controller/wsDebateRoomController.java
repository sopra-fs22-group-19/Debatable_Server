package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.Intervention;
import ch.uzh.ifi.hase.soprafs22.entity.WSMsg;
import ch.uzh.ifi.hase.soprafs22.rest.dto.InterventionPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;
import lombok.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class wsDebateRoomController {

    ///current post debates/rooms/{roomId}/msg
    // Handles messages from /debates/rooms/{roomId}/msg. (The Spring adds the /debates prefix for us).
    // Need to send to this url
    @MessageMapping("/rooms/toRoomId/msg")
    // Sends the return value of this method to /debateRoom/{roomId}
    // Need to subscribe to this url
    @SendTo("/debateRoom/roomId")
    public String getMessages(@Payload WSMsg msg) {

        String incoming = msg.getMessage();
        return incoming;
    }


}
