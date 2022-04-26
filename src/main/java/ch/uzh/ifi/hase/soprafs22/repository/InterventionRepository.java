package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long>{

    List<Intervention> findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(
            Long debateRoomRoomId, Long postingSpeakerSpeakerId);

}
