package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("debateSpeakerRepository")
public interface DebateSpeakerRepository extends JpaRepository<DebateSpeaker, Long> {

    List<DebateSpeaker> findAllByDebateRoom(DebateRoom debateRoom);

    DebateSpeaker findByDebateRoom(DebateRoom debateRoom);
    DebateSpeaker findByUserAssociatedId(Long creatorUserId);

    List<DebateSpeaker> findAllByUserAssociatedIdAndDebateRoomRoomId(Long associatedUserId, Long roomId);
}
