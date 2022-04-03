package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.repository.TagRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DebateRoomRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class DebateService {

    private final Logger log = LoggerFactory.getLogger(DebateService.class);

    private final DebateTopicRepository debateTopicRepository;
    private final DebateRoomRepository debateRoomRepository;
    private final TagRepository tagRepository;

    @Autowired
    public DebateService(
            @Qualifier("debateTopicRepository") DebateTopicRepository debateTopicRepository,
            @Qualifier("debateRoomRepository") DebateRoomRepository debateRoomRepository,
            @Qualifier("tagRepository") TagRepository tagRepository
            ) {

        this.debateTopicRepository = debateTopicRepository;
        this.debateRoomRepository = debateRoomRepository;
        this.tagRepository = tagRepository;
    }

    public DebateRoom createDebateRoom(DebateRoom inputDebateRoom, Long debateTopicId) {

        Optional<DebateTopic> debateTopic = debateTopicRepository.findById(debateTopicId);
        // DebateTopic debateTopic = new DebateTopic();
        // debateTopic.setCreatorUserId(1L);
        // debateTopic.setDebateTopicId(1L);
        // debateTopic.setTopic("Tema 1");
        // debateTopic.setTopicDescription("Tema 1 mas descrtipcion");

        if (debateTopic.isEmpty()){
            String baseErrorMessage = "Error: reason <Debate topic with id: '%d' was not found>";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(baseErrorMessage, debateTopicId));
        }

        inputDebateRoom.setDebateTopic(debateTopic.get());
        inputDebateRoom = debateRoomRepository.save(inputDebateRoom);
        debateRoomRepository.flush();

        // log.debug("Created DebateRoom: {}", newUser);
        return inputDebateRoom;

    }
}
