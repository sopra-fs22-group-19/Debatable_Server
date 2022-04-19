package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;

public class DebateRoomPutDTO {

    private Long roomId;


    public void setRoomId(Long roomId){ this.roomId = roomId; }

    public Long getRoomId(){ return this.roomId; }


}
