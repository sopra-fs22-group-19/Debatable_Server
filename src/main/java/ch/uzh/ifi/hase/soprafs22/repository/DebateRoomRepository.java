package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("debateRoomRepository")
public interface DebateRoomRepository extends JpaRepository<DebateRoom, Long> {
    DebateRoom findByRoomId(Long roomId);

    List<DebateRoom> findAllByCreatorUserId(Long creatorUserId);

    List<DebateRoom> findAllByCreatorUserIdAndDebateState(Long creatorUserId, DebateState debateState);

    @Query(value = "SELECT debateroom.* \n" +
                    "FROM ( \n" +
                    "    SELECT debate_room_room_id room_id \n" +
                    "    FROM debatespeakers \n" +
                    "    WHERE user_associated_id = :user_id \n" +
                    "     ) room_list, debateroom  \n" +
                    "WHERE room_list.room_id = debateroom.room_id ",
            nativeQuery = true)
    List<DebateRoom> findAllBySpeakerUserAssociatedId(@Param("user_id") Long userAssociatedId);

    @Query(value = "SELECT debateroom.* \n " +
                    "FROM ( \n" +
                    "    SELECT debate_room_room_id room_id \n " +
                    "    FROM debatespeakers \n" +
                    "    WHERE user_associated_id = :user_id  \n" +
                    "     ) room_list, debateroom \n" +
                    "WHERE room_list.room_id = debateroom.room_id \n" +
                    "  AND debateroom.debate_state = :debate_state \n",
            nativeQuery = true)
    List<DebateRoom> findAllBySpeakerUserAssociatedIdAndDebateState(@Param("user_id")Long userAssociatedId,
                                                                    @Param("debate_state")Integer debateState);
}
