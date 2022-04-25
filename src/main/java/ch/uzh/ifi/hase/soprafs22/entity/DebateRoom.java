package ch.uzh.ifi.hase.soprafs22.entity;


import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.exceptions.InvalidDebateStateChange;
import ch.uzh.ifi.hase.soprafs22.exceptions.SpeakerNotAllowedToPost;
import ch.uzh.ifi.hase.soprafs22.interfaces.Room;
import ch.uzh.ifi.hase.soprafs22.interfaces.RoomParticipant;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
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
@Table(name = "DEBATEROOM")
public class DebateRoom implements Serializable, Room {

  static final String NOTIMPLEMENTED = "Function not implemented yet";

  @Id
  @SequenceGenerator(
          name = "room_seq",
          sequenceName = "room_seq",
          allocationSize = 1
  )
  //avoid using same generator with userid
  //e.g.(user1 id=1 ,user2 id=2, topic1 id=3, topic2 id=4)
  @GeneratedValue(
          strategy = SEQUENCE,
          generator = "room_seq"
  )
  private Long roomId;

  @Column(nullable = false)
  private Long creatorUserId;

  @Column(nullable = false)
  private DebateState debateState = DebateState.NOT_STARTED;

  @Column(nullable = false)
  private LocalTime debateStateUpdateTime = LocalTime.now();

  @OneToMany(mappedBy="debateRoom")
  private List<DebateSpeaker> speakers = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "debate_topic_debate_topic_id")
  private DebateTopic debateTopic;

  @OneToMany(mappedBy="debateRoom")
  private List<Intervention> interventions = new ArrayList<>();

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

  public DebateState getDebateState() {  return debateState; }

  public void setDebateState(DebateState debateState) {
    this.debateState = debateState;
  }

  public LocalTime getDebateStateUpdateTime() {  return debateStateUpdateTime; }

  public void setDebateStateUpdateTime(LocalTime debateStateUpdateTime) {
        this.debateStateUpdateTime = debateStateUpdateTime;
    }

  public List<DebateSpeaker> getSpeakers() {
        return this.speakers;
    }

  public void setSpeakers(List<DebateSpeaker> speakers) { this.speakers = speakers; }

  public DebateTopic getDebateTopic() { return debateTopic; }

  public void setDebateTopic(DebateTopic debateTopic) { this.debateTopic=debateTopic; }

  public User getUser1() {
      if (speakers.isEmpty())
          return null;
      else
          return speakers.get(0).getUserAssociated();
  }

  public void setUser1(DebateSpeaker debateSpeaker) {
      if (speakers.isEmpty())
          speakers.add(debateSpeaker);
      else
          speakers.set(0, debateSpeaker);
  }

  public DebateSide getSide1() {
      if (speakers.isEmpty())
          return null;
      else
          return speakers.get(0).getDebateSide();
  }

  public User getUser2() {
      if (speakers.size() < 2)
          return null;
      else
          return speakers.get(1).getUserAssociated();
  }

  public void setUser2(DebateSpeaker debateSpeaker) {
      if (speakers.size() < 2)
          speakers.add(debateSpeaker);
      else
          speakers.set(1, debateSpeaker);
  }

  public DebateSide getSide2() {
      if (speakers.size() < 2)
          return null;
      else
          return speakers.get(1).getDebateSide();
  }

  public void setSide2(DebateSide debateSide) {  speakers.get(1).setDebateSide(debateSide); }

  public void startDebate(DebateSide debateSideStart) throws InvalidDebateStateChange {
      if (debateState != DebateState.READY_TO_START){
          String errorMessage = "The debate was not ready to start. The state of the " +
                  "debate room before starting should be: %s";
          errorMessage = String.format(errorMessage, DebateState.READY_TO_START);
          throw new InvalidDebateStateChange(errorMessage);
      }

      if (debateSideStart == DebateSide.FOR)
        setDebateState(DebateState.ONGOING_FOR);

      else if (debateSideStart == DebateSide.AGAINST)
          setDebateState(DebateState.ONGOING_AGAINST);

  }

  public void changeInterventionTurn() throws InvalidDebateStateChange {
      if (debateState == DebateState.ONGOING_FOR)
          setDebateState(DebateState.ONGOING_AGAINST);
      else if (debateState == DebateState.ONGOING_AGAINST)
          setDebateState(DebateState.ONGOING_FOR);
      else{
          String errorMessage = "The debate has not started yet. The state of the debate room should be: %s or %s";
          errorMessage = String.format(errorMessage, DebateState.ONGOING_FOR, DebateState.ONGOING_AGAINST);
          throw new InvalidDebateStateChange(errorMessage);
      }

      // TOOO: Reset timer
  }

  public void addIntervention(Intervention intervention, DebateSide speakerSide) throws SpeakerNotAllowedToPost {
      if (debateState != DebateState.ONGOING_FOR && this.debateState != DebateState.ONGOING_AGAINST){
          String errorMessage = "User cannot intervene as the debate has not started yet";
          throw new SpeakerNotAllowedToPost(errorMessage);
      }
      
      else if ((this.debateState == DebateState.ONGOING_FOR && speakerSide == DebateSide.FOR) ||
              (this.debateState == DebateState.ONGOING_AGAINST && speakerSide == DebateSide.AGAINST))
        this.interventions.add(intervention);
      
      else{
          String errorMessage = "It is not the speaker's turn to intervene";
          throw new SpeakerNotAllowedToPost(errorMessage);
      }
        

  }

  public List<Intervention> getInterventions() {
        return interventions;
    }

  public void setInterventions(List<Intervention> interventions) {
        this.interventions = interventions;
    }

  @Override
  public void registerParticipant(RoomParticipant roomParticipant) {
      throw new RuntimeException(NOTIMPLEMENTED);
  }

  @Override
  public void removeParticipant(RoomParticipant roomParticipant) {
      throw new RuntimeException(NOTIMPLEMENTED);
  }

  @Override
  public void notifyParticipants() {
        throw new RuntimeException(NOTIMPLEMENTED);
  }
}
