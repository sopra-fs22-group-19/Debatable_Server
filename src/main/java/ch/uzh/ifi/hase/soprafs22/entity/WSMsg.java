package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WSMsg {

    private Long userId;
    private String userName;
    private DebateSide userSide;
    private String message;
    private DebateState debateState;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DebateSide getUserSide() {
        return userSide;
    }

    public void setUserSide(DebateSide userSide) {
        this.userSide = userSide;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DebateState getDebateState() {
        return debateState;
    }

    public void setDebateState(DebateState debateState) {
        this.debateState = debateState;
    }

}