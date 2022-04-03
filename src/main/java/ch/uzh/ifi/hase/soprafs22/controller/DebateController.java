package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.DebateParticipantPosition;
import ch.uzh.ifi.hase.soprafs22.constant.DebateRoomStatus;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class DebateController {

    private final DebateService debateService;

    DebateController(DebateService debateService) {
        this.debateService = debateService;
    }

    @PostMapping("/debates/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public DebateRoomGetDTO createUser(@RequestBody DebateRoomPostDTO debateRoomPostDTO) {
        // convert API user to internal representation
        DebateRoom inputDebateRoom = DTOMapper.INSTANCE.convertDebateRoomPostDTOtoEntity(debateRoomPostDTO);

        // Set the state of the Debate Room depending on the side of the user that created the room
        if (inputDebateRoom.equals(DebateParticipantPosition.FOR))
            inputDebateRoom.setDebateRoomStatus(DebateRoomStatus.ONE_USER_FOR);
        else if (inputDebateRoom.equals(DebateParticipantPosition.AGAINST))
            inputDebateRoom.setDebateRoomStatus(DebateRoomStatus.ONE_USER_AGAINST);
        else{
            //TODO: Return invalid input HTTP code with the error message that side has to be "FOR" or "Against"
            System.out.println("You forgot the error code and now you get an exception!!!");
        }

        // Create the debate room in the DB
        DebateRoom createdDebateRoom = debateService.createDebateRoom(inputDebateRoom);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToDebateGetDTO(createdDebateRoom);
    }

}
