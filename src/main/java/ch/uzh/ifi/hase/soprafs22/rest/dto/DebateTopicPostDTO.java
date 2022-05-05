package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.TopicCategory;

public class DebateTopicPostDTO {

    private String topic;
    private String description;
    private TopicCategory category;

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

    public TopicCategory getCategory(){return category;}

    public void setCategory(TopicCategory category){this.category = category;}

}
