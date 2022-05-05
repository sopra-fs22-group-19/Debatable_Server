package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class DebateTopicPostDTO {

    private Long userId;
    private String topic;
    private String description;

    //TODO: Add the TAG get DTO

    public Long getUserId(){ return userId; }

    public Long setUserId(Long userId){ return this.userId = userId; }

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
