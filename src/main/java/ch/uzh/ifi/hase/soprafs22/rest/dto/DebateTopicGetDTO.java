package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Tag;

public class DebateTopicGetDTO {

    private Long userId;
    private Long debateId;
    private String topic;
    private String description;

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDebateId() { return debateId; }

    public void setDebateId(Long debateId) {
        this.debateId = debateId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
