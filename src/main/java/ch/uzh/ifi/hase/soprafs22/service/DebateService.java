package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.exceptions.InvalidDebateStateChange;
import ch.uzh.ifi.hase.soprafs22.exceptions.SpeakerNotAllowedToPost;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static ch.uzh.ifi.hase.soprafs22.entity.DebateTopic.readTopicListCSV;

@Service
@Transactional
public class DebateService {

    private final Logger log = LoggerFactory.getLogger(DebateService.class);

    private final DebateTopicRepository debateTopicRepository;
    private final DebateRoomRepository debateRoomRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final DebateSpeakerRepository debateSpeakerRepository;
    private final InterventionRepository interventionRepository;
    private final UserService userService;

    @Autowired
    public DebateService(
            @Qualifier("debateTopicRepository") DebateTopicRepository debateTopicRepository,
            @Qualifier("debateRoomRepository") DebateRoomRepository debateRoomRepository,
            @Qualifier("userRepository") UserRepository userRepository,
            @Qualifier("debateSpeakerRepository") DebateSpeakerRepository debateSpeakerRepository,
            @Qualifier("tagRepository") TagRepository tagRepository,
            @Qualifier("interventionRepository") InterventionRepository interventionRepository,
            @Qualifier("userService") UserService userService
            ) {

        this.debateTopicRepository = debateTopicRepository;
        this.debateRoomRepository = debateRoomRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.debateSpeakerRepository = debateSpeakerRepository;
        this.interventionRepository = interventionRepository;
        this.userService = userService;
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
        Path defaultListPath = Paths.get("setup", "defaultTopics.csv");

        if (log.isDebugEnabled()) { log.info(String.format("Loading default topic list from: %s", defaultListPath)); }

        try {
            // Create the default user that creates the default topics
            User defaultUserForCreatingDebateTopics = new User();
            String defaultStringFields = UUID.randomUUID().toString();
            defaultUserForCreatingDebateTopics.setName(defaultStringFields);
            defaultUserForCreatingDebateTopics.setUsername(defaultStringFields);
            defaultUserForCreatingDebateTopics.setPassword(defaultStringFields);
            defaultUserForCreatingDebateTopics.setToken(defaultStringFields);
            defaultUserForCreatingDebateTopics.setCreationDate(LocalDate.now());

            userRepository.save(defaultUserForCreatingDebateTopics);
            userRepository.flush();

            List<DebateTopic> defaultDebateTopicsList = readTopicListCSV(defaultListPath.toString(),
                    defaultUserForCreatingDebateTopics);
            if (defaultDebateTopicsList.isEmpty()){
                log.warn("List of debate topics is empty");
            } else{
                debateTopicRepository.saveAll(defaultDebateTopicsList);
                if (log.isDebugEnabled()) {
                    log.info(String.format("Default Topics created %d", defaultDebateTopicsList.size()));
                }
            }
        } catch(IOException | CsvValidationException e){
            log.error("Problem loading the default file list");
        }
    }


    public DebateRoom createDebateRoom(DebateRoom inputDebateRoom, DebateRoomPostDTO debateRoomPostDTO) {

        // Check that the debcreateDeate topic exists and add it
        Optional<DebateTopic> debateTopic = debateTopicRepository.findById(debateRoomPostDTO.getDebateId());

        if (debateTopic.isEmpty()) {
            String baseErrorMessage = "Error: reason <Debate topic with id: '%d' was not found>";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(baseErrorMessage, debateRoomPostDTO.getDebateId()));
        }

        inputDebateRoom.setDebateTopic(debateTopic.get());

        // Set the state of the debate
        if (debateRoomPostDTO.getSide() == DebateSide.FOR) {
            inputDebateRoom.setDebateState(DebateState.ONE_USER_FOR);
        } else{
            inputDebateRoom.setDebateState(DebateState.ONE_USER_AGAINST);
        }

        // Check that user that will create the debate exists and add it as a Speaker to the debate room
        Optional<User> creatingUser = userRepository.findById(debateRoomPostDTO.getUserId());

        if (creatingUser.isEmpty()){
            String baseErrorMessage = "Error: reason <User with id: '%d' was not found>";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(baseErrorMessage, debateRoomPostDTO.getUserId()));
        }

        DebateSpeaker debatesSpeaker1 = new DebateSpeaker();
        debatesSpeaker1.setUserAssociated(creatingUser.get());
        debatesSpeaker1.setDebateSide(debateRoomPostDTO.getSide());

        debatesSpeaker1.setDebateRoom(inputDebateRoom);
        inputDebateRoom.setUser1(debatesSpeaker1);

        // Store new DebateRoom in the DB
        inputDebateRoom = debateRoomRepository.save(inputDebateRoom);
        debateRoomRepository.flush();

        debateSpeakerRepository.save(debatesSpeaker1);
        debateSpeakerRepository.flush();

        log.debug("Created DebateRoom: {}", inputDebateRoom);

        return inputDebateRoom;

    }

    public List<DebateTopic> getDebateTopicByUserId(Long userId){

        User creatorUser = userRepository.findByid(userId);
        String baseErrorMessage = "Error: reason <Can not get topics because User with id: '%d' was not found>";

        if(creatorUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage,userId));
        }

        List<DebateTopic> debateTopicList = debateTopicRepository.findByIsDefaultTopicIsTrue();
        debateTopicList.addAll(debateTopicRepository.findByCreatorUser_Id(userId));
        return debateTopicList;
    }

    public DebateRoom getDebateRoom(Long roomId, String errorMessageContent) {
        DebateRoom debateRoom = debateRoomRepository.findByRoomId(roomId);

        String errorMessage = String.format("Error: reason <%s>", errorMessageContent);

        if(debateRoom == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        }
        return debateRoom;
    }

    public DebateSpeaker getDebateSpeakerByUserIdAndRoomId(Long userAssociatedId, Long roomId, String errorMessageContent) {
        List<DebateSpeaker> debateSpeaker = debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(userAssociatedId, roomId);

        String errorMessage = String.format("Error: reason <%s>", errorMessageContent);

        if(debateSpeaker == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        if (debateSpeaker.get(0) == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);

        return debateSpeaker.get(0);
    }

    public DebateRoom deleteRoom(Long roomId){
        String baseErrorMessage = String.format("Can not delete the room because Room with id: '%d' was not found", roomId);
        DebateRoom roomToDelete = getDebateRoom(roomId, baseErrorMessage);

        List<DebateSpeaker> occupiedDebateRooms = debateSpeakerRepository.findAllByDebateRoom(roomToDelete);

        if(!occupiedDebateRooms.isEmpty()){
            debateSpeakerRepository.deleteAll(occupiedDebateRooms);
            debateSpeakerRepository.flush();
        }

        debateRoomRepository.delete(roomToDelete);
        debateRoomRepository.flush();

        return roomToDelete;
    }

    public DebateRoom addParticipantToRoom(DebateRoom actualRoom, User userToAdd){

        User checkUser;

        if(Objects.isNull(userToAdd.getId())){
            checkUser = userService.createGuestUser();
        }
        else{
            checkUser = userRepository.findByid(userToAdd.getId());
        }
        String baseErrorMessage = String.format("Cannot add Participant because Room with id: '%d' was not found", actualRoom.getRoomId());
        DebateRoom updatedRoom = getDebateRoom(actualRoom.getRoomId(), baseErrorMessage);

        DebateSpeaker debatesSpeaker = new DebateSpeaker();
        debatesSpeaker.setUserAssociated(checkUser);

        if(updatedRoom.getSpeakers().get(0).getDebateSide() == DebateSide.FOR){
            debatesSpeaker.setDebateSide(DebateSide.AGAINST);
        }
        else{
            debatesSpeaker.setDebateSide(DebateSide.FOR);
        }

        debatesSpeaker.setDebateRoom(updatedRoom);
        updatedRoom.setUser2(debatesSpeaker);
        updatedRoom.setDebateState(DebateState.READY_TO_START);

        debateSpeakerRepository.save(debatesSpeaker);
        debateSpeakerRepository.flush();

        updatedRoom = debateRoomRepository.save(updatedRoom);
        debateRoomRepository.flush();

        log.debug("Participant added to the DebateRoom: {}", updatedRoom);

        return updatedRoom;
    }

    public DebateRoom setStatus(Long roomId, DebateRoom debateRoomWithNewStatus){

        String baseErrorMessage = String.format("Cannot update status because Room with id: '%d' was not found", roomId);
        DebateRoom roomToUpdate = getDebateRoom(roomId, baseErrorMessage);

        DebateState debateStatusToSet = debateRoomWithNewStatus.getDebateState();

        try{
            if (debateStatusToSet == DebateState.ONGOING_FOR
                    && roomToUpdate.getDebateState() != DebateState.ONGOING_AGAINST)
                roomToUpdate.startDebate(DebateSide.FOR);
            else if (debateStatusToSet == DebateState.ONGOING_AGAINST
                    && roomToUpdate.getDebateState() != DebateState.ONGOING_FOR)
                roomToUpdate.startDebate(DebateSide.AGAINST);
            else
                roomToUpdate.setDebateState(debateStatusToSet);
                roomToUpdate.setDebateStateUpdateTime(LocalDateTime.now());
        } catch (InvalidDebateStateChange e){
            log.error(e.toString());
            throw new ResponseStatusException(
                    HttpStatus.METHOD_NOT_ALLOWED, String.format("Error: <The debate is not ready to start>"));
        }

        debateRoomRepository.save(roomToUpdate);
        debateRoomRepository.flush();

        log.debug("Status Set to the DebateRoom: {}", roomToUpdate);


        return roomToUpdate;
    }


    public Intervention createIntervention(Intervention inputIntervention, Long roomId, Long userId) {

        DebateRoom debateRoom = getDebateRoom(roomId,
                "Cannot post message because Debate room was not found");

        inputIntervention.setDebateRoom(debateRoom);

        String baseErrorMessage = "Cannot post message because User with id: '%d' was not found";
        DebateSpeaker debateSpeaker = getDebateSpeakerByUserIdAndRoomId(
                userId, debateRoom.getRoomId(), baseErrorMessage);
        inputIntervention.setPostingSpeaker(debateSpeaker);


        // Verify if the intervention is valid by posting it to the debateRoom
        try{
            debateSpeaker.postIntervention(inputIntervention, debateRoom);
        } catch (SpeakerNotAllowedToPost e){
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Error: reason <It is not the speaker's turn>");
        }

        // Change turns for the debateRoom if the intervention is valid
        try {
            debateRoom.changeInterventionTurn();
        } catch (InvalidDebateStateChange e) {
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Error: reason <The debate has not started yet>");
        }

        // Save intervention
        Intervention newIntervention = interventionRepository.save(inputIntervention);
        interventionRepository.flush();

        // Update whose turn it is
        debateRoom = debateRoomRepository.save(debateRoom);
        debateRoomRepository.flush();

        return newIntervention;
    }


    public List<String> getUserDebateInterventions(Long roomId, Long userId, Integer topI, Integer toTopJ) {
        String errorMessage = String.format("Cannot retrieve messages because Room with id: '%d' was not found", roomId);
        DebateRoom debateRoom = getDebateRoom(roomId, errorMessage);

        errorMessage = String.format("Cannot retrieve messages because User with id: '%d' was not found", userId);
        DebateSpeaker debateSpeaker = getDebateSpeakerByUserIdAndRoomId(userId, debateRoom.getRoomId(), errorMessage);

        List<Intervention> speakerInterventions =
                interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(
                        debateRoom.getRoomId(), debateSpeaker.getSpeakerId());


        List<String> messageList = new ArrayList<>();

        if ((Objects.isNull(topI) && !Objects.isNull(toTopJ)) || (!Objects.isNull(topI) && Objects.isNull(toTopJ))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Error: reason <Either both 'top_i' and 'top_j' should be specified or neither of them " +
                            "(neither of them retrieves all messages>");
        } else if (!Objects.isNull(topI)) {
            if (topI > toTopJ){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Error: reason <toTopJ has to be larger or equal than topI>");
            } else if (topI < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Error: reason <topI has to be larger or equal than 1>");
            } else if (topI > speakerInterventions.size()){
                return new ArrayList<>();
            }

            int firstIdx = speakerInterventions.size() - toTopJ;
            firstIdx = Math.max(0, firstIdx);

            int lastIdx = speakerInterventions.size() - topI + 1;
            lastIdx = Math.max(0, lastIdx);

            speakerInterventions = speakerInterventions.subList(firstIdx, lastIdx);
        }

        for (Intervention intervention : speakerInterventions) {
            messageList.add(intervention.getMessage());
        }

        return messageList;
    }

    public DebateTopic createDebateTopic(Long userId, DebateTopic newDebateTopic) {
        // Verify if user exists
        String errorMessage = String.format("Cannot retrieve messages because User with id: '%d' was not found", userId);
        User user = userService.getUserByUserId(userId, errorMessage);

        newDebateTopic.setCreatorUser(user);

        newDebateTopic = debateTopicRepository.save(newDebateTopic);
        debateTopicRepository.flush();

        return newDebateTopic;
    }
    public List<DebateTopic> getDebateTopicByCategories(String categories){

        String baseErrorMessage = "Error: reason <Can not get categories>";

        String[] subarray = categories.split(",");

        List<DebateTopic> debateTopicList = debateTopicRepository.findAll();

        List<DebateTopic> toSend = new ArrayList<>();

        for(String toCompare: subarray){
            for(DebateTopic toAdd : debateTopicList){
                if(!Objects.isNull(toAdd.getCategory())){
                    if(toAdd.getCategory().toString().equals(toCompare)){
                        toSend.add(toAdd);
                    }
                }
            }
        }
        return toSend;
    }


    public List<DebateRoom> getDebateRoomsByUserId(Long userId, DebateState debateState) {
        // Make sure the creating user exists
        userService.getUserByUserId(userId, "The userId for which you are requesting the debates does not exit");

        List<DebateRoom> userDebateRooms;
        if (debateState != null){
            userDebateRooms = debateRoomRepository.findAllBySpeakerUserAssociatedIdAndDebateState(userId, debateState.ordinal());
        } else{

            userDebateRooms = debateRoomRepository.findAllBySpeakerUserAssociatedId(userId);
        }

        return userDebateRooms;
    }
}
