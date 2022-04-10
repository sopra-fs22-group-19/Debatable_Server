package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("debateRoomRepository")
public interface DebateRoomRepository extends JpaRepository<DebateRoom, Long> {
    DebateRoom findByRoomId(Long roomId);
    DebateRoom findByCreatorUserId(Long creatorUserId);
}
