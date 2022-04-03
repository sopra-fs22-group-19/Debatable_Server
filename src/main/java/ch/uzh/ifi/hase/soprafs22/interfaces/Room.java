package ch.uzh.ifi.hase.soprafs22.interfaces;

public interface Room {
    void registerParticipant(RoomParticipant roomParticipant);
    void removeParticipant(RoomParticipant roomParticipant);
    void notifyParticipants();

}
