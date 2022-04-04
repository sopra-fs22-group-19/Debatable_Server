package ch.uzh.ifi.hase.soprafs22.entity;


import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.interfaces.Room;
import ch.uzh.ifi.hase.soprafs22.interfaces.RoomParticipant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

  final String OPERATION_NOT_IMPLEMENTED = "Function not implemented yet";

  @Id
  @GeneratedValue
  private Long roomId;

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

  public User getUser1() {
      if (Objects.isNull(speakers))
          return null;
      else
          return speakers.get(0).getUserAssociated();
  }

  public void setUser1(DebateSpeaker debateSpeaker) {  speakers.add(0, debateSpeaker); }

  public DebateSide getSide1() {
      if (Objects.isNull(speakers))
          return null;
      else
          return speakers.get(0).getDebateSide();
  }

  public void setSide1(DebateSide debateSide) {  speakers.get(0).setDebateSide(debateSide); }

  public User getUser2() {
      if (Objects.isNull(speakers))
          return null;
      else if (speakers.size() < 2)
          return null;
      else
          return speakers.get(1).getUserAssociated();
  }

  public void setUser2(DebateSpeaker debateSpeaker) {  speakers.add(1, debateSpeaker); }

  public DebateSide getSide2() {
      if (Objects.isNull(speakers))
          return null;
      else if (speakers.size() < 2)
          return null;
      else
          return speakers.get(1).getDebateSide();
  }

  public void setSide2(DebateSide debateSide) {  speakers.get(1).setDebateSide(debateSide); }

    @Override
  public void registerParticipant(RoomParticipant roomParticipant) {
      throw new RuntimeException(OPERATION_NOT_IMPLEMENTED);
  }

  @Override
  public void removeParticipant(RoomParticipant roomParticipant) {
      throw new RuntimeException(OPERATION_NOT_IMPLEMENTED);
  }

  @Override
  public void notifyParticipants() {
        throw new RuntimeException(OPERATION_NOT_IMPLEMENTED);
  }
}
