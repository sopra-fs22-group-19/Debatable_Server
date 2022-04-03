package ch.uzh.ifi.hase.soprafs22.entity;


import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.interfaces.Room;
import ch.uzh.ifi.hase.soprafs22.interfaces.RoomParticipant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
public class DebateRoom implements Serializable, Room {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long roomId;

  @Column(nullable = false)
  private Long debateTopicId;

  @Column(nullable = false)
  private Long creatorUserId;

  @Column(nullable = false)
  private DebateState debateStatus;

  @OneToMany(mappedBy="debateRoom")
  private List<DebateSpeaker> speakers;

  @ManyToOne
  @JoinColumn(name = "debate_topic_debate_topic_id")
  private DebateTopic debateTopic;


  public Long getRoomId() {
    return roomId;
  }

  public void setRoomId(Long roomId) {
    this.roomId = roomId;
  }

  public Long getDebateTopicId() {
        return debateTopicId;
    }

  public void setDebateTopicId(Long debateTopicId) {
        this.debateTopicId = debateTopicId;
    }

  public Long getCreatorUserId() { return creatorUserId; }

  public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

  public DebateState getDebateRoomStatus() {  return debateStatus; }

  public void setDebateRoomStatus(DebateState debateStatus) {
    this.debateStatus = debateStatus;
  }

  public List<DebateSpeaker> getSpeakers() {
        return this.speakers;
    }

  public void setSpeakers(List<DebateSpeaker> speakers) { this.speakers = speakers; }

  public DebateTopic getDebateTopic() { return debateTopic; }

  public void setDebateTopic(DebateTopic debateTopic) { this.debateTopic=debateTopic; }

  @Override
  public void registerParticipant(RoomParticipant roomParticipant) {
      throw new RuntimeException("Function not implemented yet");
  }

  @Override
  public void removeParticipant(RoomParticipant roomParticipant) {
      throw new RuntimeException("Function not implemented yet");
  }

  @Override
  public void notifyParticipants() {
        throw new RuntimeException("Function not implemented yet");
  }
}
