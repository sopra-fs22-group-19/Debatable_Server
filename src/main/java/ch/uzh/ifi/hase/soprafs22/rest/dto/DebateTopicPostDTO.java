package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class DebateTopicPostDTO {

    private String topic;
    private String description;

    //TODO: Add the TAG get DTO


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
