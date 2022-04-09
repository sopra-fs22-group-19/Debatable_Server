package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;


public class DebateRoomGetDTO {
    private Long roomId;
    private DebateTopicGetDTO debate;
    private DebateState debateStatus;
    private UserGetDTO user1;
    private DebateSide side1;
    private UserGetDTO user2;
    private DebateSide side2;


    public void setRoomId(Long roomId){ this.roomId = roomId; }

    public Long getRoomId(){ return this.roomId; }

    public void setDebateStatus(DebateState debateStatus) {
        this.debateStatus = debateStatus;
    }

    public DebateState getDebateStatus() {
        return this.debateStatus;
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
