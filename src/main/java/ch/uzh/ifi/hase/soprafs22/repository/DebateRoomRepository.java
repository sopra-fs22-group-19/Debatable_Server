package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("debateRoomRepository")
public interface DebateRoomRepository extends JpaRepository<DebateRoom, Long> {
    DebateRoom findByRoomId(Long roomId);
    List<DebateRoom> findByCreatorUserId(Long creatorUserId);

    List<DebateRoom> findAllByCreatorUserIdAndDebateState(Long creatorUserId, DebateState debateState);
}
