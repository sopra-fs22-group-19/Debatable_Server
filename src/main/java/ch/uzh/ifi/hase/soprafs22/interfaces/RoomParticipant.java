package ch.uzh.ifi.hase.soprafs22.interfaces;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.Intervention;

public interface RoomParticipant {
    void update();
    void postIntervention(Intervention intervention, DebateRoom debateRoom);
}
