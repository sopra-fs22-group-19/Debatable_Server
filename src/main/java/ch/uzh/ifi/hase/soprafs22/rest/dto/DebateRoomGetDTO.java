package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;

public class DebateRoomGetDTO {

    private Long roomId;
    private DebateTopicGetDTO debate;
    private String debateStatus;
    private UserGetDTO user1;
    private DebateSide side1;
    private UserGetDTO user2;
    private DebateSide side2;

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

    public UserGetDTO getUser1() {
        return user1;
    }

    public void setUser1(UserGetDTO user1) {
        this.user1 = user1;
    }

    public DebateSide getSide1() {
        return side1;
    }

    public void setSide1(DebateSide side1) {
        this.side1 = side1;
    }

    public UserGetDTO getUser2() {
        return user2;
    }

    public void setUser2(UserGetDTO user2) {
        this.user2 = user2;
    }

    public DebateSide getSide2() {
        return side2;
    }

    public void setSide2(DebateSide side2) {
        this.side2 = side2;
    }
}
