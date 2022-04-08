package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateRoomRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.TagRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class DebateService {

    private final Logger log = LoggerFactory.getLogger(DebateService.class);

    private final DebateTopicRepository debateTopicRepository;
    private final DebateRoomRepository debateRoomRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Autowired
    public DebateService(
            @Qualifier("debateTopicRepository") DebateTopicRepository debateTopicRepository,
            @Qualifier("debateRoomRepository") DebateRoomRepository debateRoomRepository,
            @Qualifier("userRepository") UserRepository userRepository,
            @Qualifier("tagRepository") TagRepository tagRepository
            ) {

        this.debateTopicRepository = debateTopicRepository;
        this.debateRoomRepository = debateRoomRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @PostConstruct
    private void setupDefaultDebateTopics(){
        log.info("Setup default debate topics");

        // Check if the debate repository is empty
        if (debateTopicRepository.count() != 0L){
            log.info("Default debate topics already created");
            return;
        }

        // If it is not empty load the default topics from file
        try {

            Path defaultListPath = Paths.get("setup", "defaultTopics.csv");

            log.info(String.format("Loading default topic list from: %s", defaultListPath));

            CSVReader csvReader = new CSVReaderBuilder(new FileReader(defaultListPath.toString()))
                    .withSkipLines(1).build();

            String[] line;
            int i = 0;
            while ((line = csvReader.readNext()) != null) {
                DebateTopic defaultDebateTopic = new DebateTopic();
                defaultDebateTopic.setTopic(line[0]);
                defaultDebateTopic.setTopicDescription(line[1]);
                defaultDebateTopic.setCreatorUserId(-1L);

                debateTopicRepository.save(defaultDebateTopic);
                i++;
            }
            log.info(String.format("Default Topics created %d", i));
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }


    public DebateRoom createDebateRoom(DebateRoom inputDebateRoom, DebateRoomPostDTO debateRoomPostDTO) {

        Optional<DebateTopic> debateTopic = debateTopicRepository.findById(debateRoomPostDTO.getDebateId());

        if (debateTopic.isEmpty()){
            String baseErrorMessage = "Error: reason <Debate topic with id: '%d' was not found>";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(baseErrorMessage, debateRoomPostDTO.getDebateId()));
        }

        inputDebateRoom.setDebateTopic(debateTopic.get());

        // Create Speaker that will create the debate
        Optional<User> creatingUser = userRepository.findById(debateRoomPostDTO.getUserId());

        if (creatingUser.isEmpty()){
            String baseErrorMessage = "Error: reason <User with id: '%d' was not found>";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(baseErrorMessage, debateRoomPostDTO.getUserId()));
        }

        DebateSpeaker debatesSpeaker1 = new DebateSpeaker();
        debatesSpeaker1.setUserAssociated(creatingUser.get());

        inputDebateRoom.setSpeakers(new ArrayList<>());
        inputDebateRoom.setUser1(debatesSpeaker1);
        inputDebateRoom = debateRoomRepository.save(inputDebateRoom);
        debateRoomRepository.flush();

        log.debug("Created DebateRoom: {}", inputDebateRoom);

        return inputDebateRoom;

    }

    public Optional<DebateRoom> getDebateRoom(Long roomId) {
        return debateRoomRepository.findById(roomId);
    }
}
