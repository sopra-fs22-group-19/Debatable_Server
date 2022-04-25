package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.interfaces.RoomParticipant;

import javax.persistence.*;


@Entity
@Table(name = "DEBATESPEAKERS")
public class DebateSpeaker implements RoomParticipant {
    static final String NOTIMPLEMENTED = "Function not implemented yet";

    @Id
    @GeneratedValue
    private Long speakerId;

    @ManyToOne
    @JoinColumn(name = "user_associated_id", nullable = false)
    private User userAssociated;

    @ManyToOne
    @JoinColumn(name = "debate_room_room_id", nullable = false)
    private DebateRoom debateRoom;

    @Column(nullable = false)
    private DebateSide debateSide;

    public Long getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(Long speakerId) {
        this.speakerId = speakerId;
    }

    public User getUserAssociated() { return userAssociated; }

    public void setUserAssociated(User userAssociated) { this.userAssociated = userAssociated; }

    public DebateRoom getDebateRoom() { return debateRoom; }

    public void setDebateRoom(DebateRoom debateRoom) { this.debateRoom = debateRoom; }

    public DebateSide getDebateSide() { return debateSide; }

    public void setDebateSide(DebateSide debateSide) { this.debateSide = debateSide; }

    @Override
    public void update() {
        throw new RuntimeException(NOTIMPLEMENTED);
    }

    @Override
    public void postIntervention(Intervention intervention, DebateRoom debateRoom) {
        throw new RuntimeException(NOTIMPLEMENTED);
    }
}
