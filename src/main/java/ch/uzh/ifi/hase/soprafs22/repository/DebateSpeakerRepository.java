package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("debateSpeakerRepository")
public interface DebateSpeakerRepository extends JpaRepository<DebateSpeaker, Long> {

    DebateSpeaker findByUserAssociatedId(Long creatorUserId);
}
