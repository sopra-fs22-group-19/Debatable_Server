package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("debateTopicRepository")
public interface DebateTopicRepository extends JpaRepository<DebateTopic, Long> {

    List<DebateTopic> findByCreatorUserId(Long creatorUserId);

    List<DebateTopic> findByIsDefaultTopicIsTrue();

}
