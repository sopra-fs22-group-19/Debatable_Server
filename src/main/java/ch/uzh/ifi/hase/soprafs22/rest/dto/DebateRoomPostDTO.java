package ch.uzh.ifi.hase.soprafs22.rest.dto;

import javax.persistence.Column;

public class DebateRoomPostDTO {

  private Long creatorUserId;

  private Long debateTopicId;

  private String side;

  public Long getCreatorUserId() { return creatorUserId; }

  public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

  public Long getDebateTopicId() {
        return debateTopicId;
    }

  public void setDebateTopicId(Long debateTopicId) {
        this.debateTopicId = debateTopicId;
    }

  public String getSide() {
        return side;
    }

  public void setSide(String side) {
        this.side = side;
    }


}
