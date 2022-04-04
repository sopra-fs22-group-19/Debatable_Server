package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("tagRepository")
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    Tag findByIsFilterTag(Boolean isFilterTag);
}
