package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

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
@Table(name = "DEBATETOPICS")
public class DebateTopic implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(
          name = "topic_seq",
          sequenceName = "topic_seq",
          allocationSize = 1
  )
  //avoid using same generator with userid
  //e.g.(user1 id=1 ,user2 id=2, topic1 id=3, topic2 id=4)
  @GeneratedValue(
          strategy = SEQUENCE,
          generator = "topic_seq"
  )
  private Long debateTopicId;

  @Column(nullable = false)
  private Long creatorUserId;

  @Column(nullable = false)
  private String topic;

  @Column
  private String topicDescription;

  @OneToMany(mappedBy="debateTopic")
  private List<DebateRoom> debateRoomSet;

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

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getTopicDescription() {
        return topicDescription;
    }

  public void setTopicDescription(String topicDescription) {
    this.topicDescription = topicDescription;
  }

}
