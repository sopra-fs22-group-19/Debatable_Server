package ch.uzh.ifi.hase.soprafs22.interfaces;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.Intervention;
import ch.uzh.ifi.hase.soprafs22.exceptions.SpeakerNotAllowedToPost;

public interface RoomParticipant {
    void update();
    void postIntervention(Intervention intervention, DebateRoom debateRoom) throws SpeakerNotAllowedToPost;
}
