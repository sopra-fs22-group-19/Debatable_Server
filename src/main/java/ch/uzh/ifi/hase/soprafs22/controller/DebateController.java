package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DebateController {

    private final DebateService debateService;

    DebateController(DebateService debateService) {
        this.debateService = debateService;
    }

    @PostMapping("/debates/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public DebateRoomGetDTO createDebateRoom(@RequestBody DebateRoomPostDTO debateRoomPostDTO) {
        // convert API user to internal representation
        DebateRoom inputDebateRoom = DTOMapper.INSTANCE.convertDebateRoomPostDTOtoEntity(debateRoomPostDTO);

             // Create the debate room in the DB
        DebateRoom createdDebateRoom = debateService.createDebateRoom(inputDebateRoom, debateRoomPostDTO);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToDebateRoomGetDTO(createdDebateRoom);
    }


    @GetMapping("/debates/rooms/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DebateRoomGetDTO getDebateRoom(@PathVariable Long roomId) {
        DebateRoom debateRoom = debateService.getDebateRoom(roomId);

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
        List<DebateTopic> debateTopics = debateService.getDebateTopicByUserId(userId);

        for (DebateTopic debateTopic : debateTopics) {
            debateGetDTOs.add(DTOMapper.INSTANCE.convertEntityToDebateGetDTO(debateTopic));
        }

        return debateGetDTOs;
    }

    @DeleteMapping("/debates/rooms/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteDebateRoomById(@PathVariable("roomId") Long roomId){
        debateService.deleteRoom(roomId);
    }

    @PutMapping("/debates/rooms/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public DebateRoomGetDTO addSecondParticipantById(@PathVariable("roomId") Long roomId, @RequestBody UserPutDTO userPutDTO){

        DebateRoom toUpdateRoom = new DebateRoom();
        toUpdateRoom.setRoomId(roomId);
        User userToAdd = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        DebateRoom updatedRoom = debateService.addParticipantToRoom(toUpdateRoom, userToAdd);

        return DTOMapper.INSTANCE.convertEntityToDebateRoomGetDTO(updatedRoom);
    }

    @PutMapping("/debates/status/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public DebateRoomGetDTO updateStatus(@PathVariable("roomId") Long roomId, @RequestBody Integer status){

        DebateRoom updatedRoom = debateService.setStatus(roomId, status);

        return DTOMapper.INSTANCE.convertEntityToDebateRoomGetDTO(updatedRoom);
    }

}
