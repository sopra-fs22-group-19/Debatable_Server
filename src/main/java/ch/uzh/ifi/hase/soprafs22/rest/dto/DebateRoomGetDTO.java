package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.DebateRoomStatus;

public class DebateRoomGetDTO {

    private Long roomId;
    private DebateRoomStatus status;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public DebateRoomStatus getDebateRoomStatus() {
        return status;
    }

    public void setDebateRoomStatus(DebateRoomStatus status) {
        this.status = status;
    }
}
