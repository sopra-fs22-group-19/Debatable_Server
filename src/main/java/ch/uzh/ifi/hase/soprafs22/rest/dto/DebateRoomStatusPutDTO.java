package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.DebateState;

public class DebateRoomStatusPutDTO {

    private DebateState debateState;


    public void setDebateState(DebateState debateState){ this.debateState = debateState; }

    public DebateState getDebateState(){ return this.debateState; }
}
