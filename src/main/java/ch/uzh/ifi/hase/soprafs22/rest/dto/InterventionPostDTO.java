package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.util.Date;

public class InterventionPostDTO {


    private Long roomId;
    private Long UserId;
    private String messageContent;


    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
