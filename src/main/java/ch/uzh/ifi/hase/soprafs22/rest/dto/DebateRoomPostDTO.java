package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;

public class DebateRoomPostDTO {

  private Long userId;

  private Long debateId;

  private DebateSide side;

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

  public DebateSide getSide() { return side; }

  public void setSide(DebateSide side) {
        this.side = side;
    }


}
