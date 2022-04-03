package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "DEBATETOPIC")
public class DebateTopic implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long debateTopicId;

  @Column(nullable = false)
  private Long creatorUserId;

  @Column(nullable = false)
  private String topic;

  @Column
  private String topicDescription;

  //TODO: Iterable list of tags. How to tell JPA to store a table with the one to many realtionship

  public Long getDebateTopicId() {
    return debateTopicId;
  }

  public void setDebateTopicId(Long debateTopicId) {
    this.debateTopicId = debateTopicId;
  }

  public Long getCreatorUserId() {
        return creatorUserId;
    }

  public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String name) {
    this.topic = topic;
  }

  public String getTopicDescription() {
        return topicDescription;
    }

  public void setTopicDescription(String topicDescription) {
    this.topicDescription = topicDescription;
  }

}
