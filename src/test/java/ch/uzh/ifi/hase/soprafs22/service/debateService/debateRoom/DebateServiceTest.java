package ch.uzh.ifi.hase.soprafs22.service.debateService.debateRoom;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.InterventionPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class DebateServiceTest {

  @Mock
  private DebateRoomRepository debateRoomRepository;

  @Mock
  private DebateTopicRepository debateTopicRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private InterventionRepository interventionRepository;

  @Mock
  private DebateSpeakerRepository debateSpeakerRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private DebateService debateService;

  private DebateRoom testDebateRoom;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    User creatingUser = new User();
    creatingUser.setId(1L);
    creatingUser.setUsername("test username");
    creatingUser.setName("test user's name");
    creatingUser.setCreationDate(LocalDate.parse("2019-01-21"));
    creatingUser.setToken("lajflfa");

    DebateSpeaker debatesSpeaker1 = new DebateSpeaker();
    debatesSpeaker1.setUserAssociated(creatingUser);
    debatesSpeaker1.setDebateSide(DebateSide.FOR);

    DebateTopic testDebateTopic = new DebateTopic();
    testDebateTopic.setCreatorUserId(1L);
    testDebateTopic.setDebateTopicId(1L);
    testDebateTopic.setTopic("Topic 1");
    testDebateTopic.setTopicDescription("Topic 1' description");

    testDebateRoom = new DebateRoom();
    testDebateRoom.setRoomId(1L);
    testDebateRoom.setCreatorUserId(1L);
    testDebateRoom.setDebateTopic(testDebateTopic);
    testDebateRoom.setUser1(debatesSpeaker1);


      // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(debateRoomRepository.save(Mockito.any())).thenReturn(testDebateRoom);
    Mockito.when(debateSpeakerRepository.save(Mockito.any())).thenReturn(debatesSpeaker1);
    Mockito.when(debateTopicRepository.findById(Mockito.any())).thenReturn(Optional.of(testDebateTopic));
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(creatingUser));
  }

  @Test
  void createDebateRoom_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    DebateRoomPostDTO debateRoomPostDTO = new DebateRoomPostDTO();
    debateRoomPostDTO.setUserId(1L);
    debateRoomPostDTO.setDebateId(1L);
    debateRoomPostDTO.setSide(DebateSide.FOR);

    DebateRoom createdDebateRoom = debateService.createDebateRoom(testDebateRoom, debateRoomPostDTO);

    // then
    Mockito.verify(debateRoomRepository, Mockito.times(1)).save(Mockito.any());
    Mockito.verify(debateSpeakerRepository, Mockito.times(1)).save(Mockito.any());


    assertEquals(testDebateRoom.getRoomId(), createdDebateRoom.getRoomId());
    assertEquals(testDebateRoom.getCreatorUserId(), createdDebateRoom.getCreatorUserId());
    assertEquals(testDebateRoom.getDebateRoomStatus(), createdDebateRoom.getDebateRoomStatus());
    assertEquals(testDebateRoom.getDebateTopic(), createdDebateRoom.getDebateTopic());
    assertEquals(testDebateRoom.getSpeakers(), createdDebateRoom.getSpeakers());
  }

  @Test
  void createDebateRoom_creatingUserNotFound() {
      // when -> any object is being save in the userRepository -> return the dummy
      DebateRoomPostDTO debateRoomPostDTO = new DebateRoomPostDTO();
      debateRoomPostDTO.setUserId(2L);
      debateRoomPostDTO.setDebateId(1L);
      debateRoomPostDTO.setSide(DebateSide.FOR);

      Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

      assertThrows(ResponseStatusException.class, () ->
              debateService.createDebateRoom(testDebateRoom, debateRoomPostDTO));
  }

  @Test
  void createDebateRoom_debateTopicNotFound() {
      // when -> any object is being save in the userRepository -> return the dummy
      DebateRoomPostDTO debateRoomPostDTO = new DebateRoomPostDTO();
      debateRoomPostDTO.setUserId(1L);
      debateRoomPostDTO.setDebateId(2L);
      debateRoomPostDTO.setSide(DebateSide.FOR);

      Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testDebateRoom.getUser1()));
      Mockito.when(debateTopicRepository.findById(Mockito.any())).thenReturn(Optional.empty());

      assertThrows(ResponseStatusException.class, () ->
              debateService.createDebateRoom(testDebateRoom, debateRoomPostDTO));
  }

  @Test
  void deleteDebateRoom_Sucess() {
      Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testDebateRoom.getUser1()));
      Mockito.when(debateTopicRepository.findById(Mockito.any())).thenReturn(Optional.empty());

      Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);
      Mockito.when(debateSpeakerRepository.findAllByDebateRoom(Mockito.any())).thenReturn(testDebateRoom.getSpeakers());

      Throwable throwable = catchThrowable(() -> debateService.deleteRoom(testDebateRoom.getRoomId()));
      assertNull(throwable);
  }

  @Test
  void deleteDebateRoom_RoomNotFound() {
      Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(null);

      Long roomId = testDebateRoom.getRoomId();
      assertThrows(ResponseStatusException.class, () -> debateService.deleteRoom(roomId));
  }

    @Test
    void createIntervention_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();
        interventionPostDTO.setRoomId(1L);
        interventionPostDTO.setUserId(1L);
        interventionPostDTO.setMessageContent("test_msg");

        Intervention inputIntervention = new Intervention();
        inputIntervention.setMessage("test_msg");

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test username");
        testUser.setName("test user's name");
        testUser.setCreationDate(LocalDate.parse("2019-01-21"));
        testUser.setToken("lajflfa");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        Intervention newIntervention = new Intervention();
        newIntervention.setMsgId(1L);
        newIntervention.setPostUser(testUser);
        newIntervention.setDebateRoom(testDebateRoom);
        newIntervention.setMessage("test_msg");
        newIntervention.setTimestamp(Date.valueOf("2019-01-21"));

        Mockito.when(interventionRepository.save(Mockito.any())).thenReturn(newIntervention);

        Intervention savedIntervention = debateService.createIntervention(inputIntervention, interventionPostDTO);

        assertEquals(interventionPostDTO.getMessageContent(), savedIntervention.getMessage());
        assertEquals(interventionPostDTO.getUserId(),savedIntervention.getPostUser().getId());
        assertEquals(interventionPostDTO.getRoomId(),savedIntervention.getDebateRoom().getRoomId());
    }

    @Test
    void createIntervention_wrongRoomId_notfound(){
        // when -> setup additional mocks for UserRepository
        Intervention inputIntervention = new Intervention();
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class,
                () -> debateService.createIntervention(inputIntervention, interventionPostDTO));
    }

    @Test
    void createIntervention_wrongUserId_notfound(){
        // when -> setup additional mocks for UserRepository
        Intervention inputIntervention = new Intervention();
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class,
                () -> debateService.createIntervention(inputIntervention, interventionPostDTO));
    }

    @Test
    void changeStatus_wrongRoomId_notFound(){

        DebateRoom testRoom = new DebateRoom();
        testRoom.setRoomId(1L);

        Mockito.when(debateRoomRepository.findById(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class,
                () -> debateService.setStatus(testRoom.getRoomId(),4));

    }

    @Test
    void changeStatus_wrongStatusInput_Unauthorized(){

        DebateState[] states = DebateState.values();
        Integer invalidState = states.length + 1;

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        assertThrows(ResponseStatusException.class, () -> debateService.setStatus(testDebateRoom.getRoomId(), invalidState));

    }

    @Test
    void changeStatus_SuccessfulTry(){

        DebateState[] states = DebateState.values();
        int state;

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        for(state = 0; state < states.length; state++){

            DebateRoom successTry = debateService.setStatus(testDebateRoom.getRoomId() ,state);
            assertEquals(states[state], successTry.getDebateRoomStatus());
        }
    }

    @Test
    void addSecondParticipant_RoomIdNotFound(){

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test username");
        testUser.setName("test user's name");
        testUser.setCreationDate(LocalDate.parse("2019-01-21"));
        testUser.setToken("lajflfa");

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));
        Mockito.when(userService.createGuestUser(Mockito.any())).thenReturn(testUser);

        DebateRoom testRoom = new DebateRoom();
        testRoom.setRoomId(-1L);

        assertThrows(ResponseStatusException.class, () -> debateService.addParticipantToRoom(testRoom, testUser));
    }

    @Test
    void addSecondParticipant_Success(){
        User testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("test username 2");
        testUser.setName("test user's name 2");
        testUser.setCreationDate(LocalDate.parse("2019-01-21"));
        testUser.setToken("lajflfa");

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);
        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        DebateRoom updatedRoom = debateService.addParticipantToRoom(testDebateRoom, testUser);
        Mockito.verify(debateRoomRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(updatedRoom.getUser2().getId(), testUser.getId());

    }
}
