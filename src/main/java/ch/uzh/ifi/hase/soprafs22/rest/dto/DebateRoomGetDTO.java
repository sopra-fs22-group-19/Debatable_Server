package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.DebateRoomStatus;

public class DebateRoomGetDTO {

    private Long roomId;
    private String debateStatus;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getDebateStatus() {
        return debateStatus;
    }

    public void setDebateStatus(String debateStatus) {
        this.debateStatus = debateStatus;
    }
}
