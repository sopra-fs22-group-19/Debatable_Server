package ch.uzh.ifi.hase.soprafs22.rest.dto;

import javax.persistence.Column;

public class DebateRoomPostDTO {

  private Long userId;

  private Long debateId;

  private String side;

  public Long getUserId() { return userId; }

  public void setUserId(Long userId) {
        this.userId = userId;
    }

  public Long getDebateId() {
        return debateId;
    }

  public void setDebateId(Long debateId) {
        this.debateId = debateId;
    }

  public String getSide() {
        return side;
    }

  public void setSide(String side) {
        this.side = side;
    }


}
