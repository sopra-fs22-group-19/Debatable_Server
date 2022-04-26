package ch.uzh.ifi.hase.soprafs22.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;


@Entity
@Table(name = "INTERVENTION")
public class Intervention implements Serializable {

    @Id
    @SequenceGenerator(
            name = "msg_seq",
            sequenceName = "msg_seq",
            allocationSize = 1
    )
    //avoid using same generator with userid
    //e.g.(user1 id=1 ,user2 id=2, topic1 id=3, topic2 id=4)
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "msg_seq"
    )
    private Long msgId;

    @ManyToOne
    @JoinColumn(name = "debate_room_room_id", nullable = false)
    private DebateRoom debateRoom;

    @ManyToOne
    @JoinColumn(name = "posting_speaker_speaker_id", nullable = false)
    private DebateSpeaker postingSpeaker;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String message;


    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public DebateRoom getDebateRoom() {
        return debateRoom;
    }

    public void setDebateRoom(DebateRoom debateRoom) {
        this.debateRoom = debateRoom;
    }

    public DebateSpeaker getPostingSpeaker() {
        return postingSpeaker;
    }

    public void setPostingSpeaker(DebateSpeaker postingSpeaker) {
        this.postingSpeaker = postingSpeaker;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
