package ch.uzh.ifi.hase.soprafs22.entity;


import ch.uzh.ifi.hase.soprafs22.constant.DebateRoomStatus;

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
@Table(name = "DEBATEROOM")
public class DebateRoom implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long roomId;

  @Column(nullable = false)
  private Long debateTopicId;

  @Column(nullable = false)
  private Long creatorUserId;

  @Column(nullable = false)
  private DebateRoomStatus debateStatus;

  //TODO: Iterable of SpeakersInFavor. How to tell JPA to store a table with the one to many realtionship

  //TODO: Iterable of SpeakersInFavor. How to tell JPA to store a table with the one to many realtionship

  //TODO: Iterable of Interventions. How to tell JPA to store a table with the one to many realtionship

  public Long getRoomId() {
    return roomId;
  }

  public void setRoomId(Long roomId) {
    this.roomId = roomId;
  }

  public Long getDebateTopicId() {
        return roomId;
    }

  public void setDebateTopicId(Long debateTopicId) {
        this.debateTopicId = debateTopicId;
    }

  public Long getCreatorUserId() {
        return roomId;
    }

  public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

  public DebateRoomStatus getDebateRoomStatus() {
    return debateStatus;
  }

  public void setDebateRoomStatus(DebateRoomStatus debateStatus) {
    this.debateStatus = debateStatus;
  }


}
