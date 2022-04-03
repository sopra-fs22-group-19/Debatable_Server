package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;

public class DebateRoomGetDTO {

    private Long roomId;

    private DebateTopicGetDTO debate;

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

    public DebateTopicGetDTO getDebate() {
        return debate;
    }

    public void setDebate(DebateTopicGetDTO debateTopicGetDTO) {
        this.debate = debateTopicGetDTO;
    }
}
