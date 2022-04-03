package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;

public class DebateRoomGetDTO {

    private Long roomId;
    private String debateStatus;
    private DebateTopic debateTopic;

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

    public DebateTopic getDebateTopic() {
        return debateTopic;
    }

    public void setDebateTopic(DebateTopic debateTopic) {
        this.debateTopic = debateTopic;
    }
}
