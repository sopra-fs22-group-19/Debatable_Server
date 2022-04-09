package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateTopicGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.DebateRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DebateRoomController {

    private final DebateRoomService debateRoomService;

    DebateRoomController(DebateRoomService debateRoomService) {
        this.debateRoomService = debateRoomService;
    }

    @PostMapping("/debates/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public DebateRoomGetDTO createDebateRoom(@RequestBody DebateRoomPostDTO debateRoomPostDTO) {
        // convert API user to internal representation
        DebateRoom inputDebateRoom = DTOMapper.INSTANCE.convertDebateRoomPostDTOtoEntity(debateRoomPostDTO);

             // Create the debate room in the DB
        DebateRoom createdDebateRoom = debateRoomService.createDebateRoom(inputDebateRoom, debateRoomPostDTO);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToDebateRoomGetDTO(createdDebateRoom);
    }


    @GetMapping("/debates/rooms/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DebateRoomGetDTO getDebateRoom(@PathVariable Long roomId) {
        DebateRoom debateRoom = debateRoomService.getDebateRoom(roomId);

        if (debateRoom == null) {
            String baseErrorMessage = "Error: <the Debate Room with id: '%d' was not found>";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(baseErrorMessage, roomId));
        }
        else{
            return DTOMapper.INSTANCE.convertEntityToDebateRoomGetDTO(debateRoom);
        }
    }

    @GetMapping("/debates/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<DebateTopicGetDTO> getTopicByUser(@PathVariable Long userId){

        List<DebateTopicGetDTO> debateGetDTOs = new ArrayList<>();
        List<DebateTopic> debateTopics = debateRoomService.getDebateTopicByUserId(userId);

        for (DebateTopic debateTopic : debateTopics) {
            debateGetDTOs.add(DTOMapper.INSTANCE.convertEntityToDebateGetDTO(debateTopic));
        }

        return debateGetDTOs;
    }

    @DeleteMapping("/debates/rooms/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteDebateRoomById(@PathVariable("roomId") Long roomId){
        debateRoomService.deleteRoom(roomId);
    }
}
