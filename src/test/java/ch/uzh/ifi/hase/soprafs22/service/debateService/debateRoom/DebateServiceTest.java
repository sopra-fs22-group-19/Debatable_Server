package ch.uzh.ifi.hase.soprafs22.service.debateService.debateRoom;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

  private User creatingUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    creatingUser = new User();
    creatingUser.setId(1L);
    creatingUser.setUsername("test username");
    creatingUser.setName("test user's name");
    creatingUser.setCreationDate(LocalDate.parse("2019-01-21"));
    creatingUser.setToken("lajflfa");

    DebateSpeaker debatesSpeaker1 = new DebateSpeaker();
    debatesSpeaker1.setUserAssociated(creatingUser);
    debatesSpeaker1.setDebateSide(DebateSide.FOR);

    DebateTopic testDebateTopic = new DebateTopic();
    testDebateTopic.setCreatorUser(creatingUser);
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
    assertEquals(testDebateRoom.getDebateState(), createdDebateRoom.getDebateState());
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
  void changeStatus_wrongRoomId_notFound(){

      DebateRoom testRoom = new DebateRoom();
      testRoom.setDebateState(DebateState.ONGOING_FOR);
      Long testRoomId  = 1L;

      Mockito.when(debateRoomRepository.findById(Mockito.any())).thenReturn(null);

      assertThrows(ResponseStatusException.class,
              () -> debateService.setStatus(testRoomId, testRoom));

  }

  @Test
  void changeStatus_AnyStateBesidesOngoing_Successful(){
      testDebateRoom.setDebateState(DebateState.NOT_STARTED);
      DebateRoom debateRoomWithStateToUpdate = new DebateRoom();
      debateRoomWithStateToUpdate.setDebateState(DebateState.ENDED);

      Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

      DebateRoom updatedDebateRoom = debateService.setStatus(testDebateRoom.getRoomId(), debateRoomWithStateToUpdate);
      assertEquals(updatedDebateRoom.getDebateState(), debateRoomWithStateToUpdate.getDebateState());
  }

  @Test
  void changeStatus_Ongoing_Successful() {
      testDebateRoom.setDebateState(DebateState.READY_TO_START);

      // ONGOING_FOR
      DebateRoom debateRoomWithStateToUpdate = new DebateRoom();
      debateRoomWithStateToUpdate.setDebateState(DebateState.ONGOING_FOR);

      Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

      DebateRoom updatedDebateRoom = debateService.setStatus(testDebateRoom.getRoomId(), debateRoomWithStateToUpdate);
      assertEquals(updatedDebateRoom.getDebateState(), debateRoomWithStateToUpdate.getDebateState());

      // ONGOING_AGAINST
      testDebateRoom.setDebateState(DebateState.READY_TO_START);
      debateRoomWithStateToUpdate.setDebateState(DebateState.ONGOING_AGAINST);

      Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

      updatedDebateRoom = debateService.setStatus(testDebateRoom.getRoomId(), debateRoomWithStateToUpdate);
      assertEquals(updatedDebateRoom.getDebateState(), debateRoomWithStateToUpdate.getDebateState());

  }

  @Test
  void changeStatus_Ongoing_FailNotRightStateForTransition() {
      testDebateRoom.setDebateState(DebateState.ONE_USER_AGAINST);
      Long testRoomId = testDebateRoom.getRoomId();

      // ONGOING_FOR
      DebateRoom debateRoomWithStateToUpdate = new DebateRoom();
      debateRoomWithStateToUpdate.setDebateState(DebateState.ONGOING_FOR);

      Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

      assertThrows(ResponseStatusException.class,
              () -> debateService.setStatus(testRoomId, debateRoomWithStateToUpdate));

      // ONGOING_AGAINST
      debateRoomWithStateToUpdate.setDebateState(DebateState.ONGOING_AGAINST);

      Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

      assertThrows(ResponseStatusException.class,
              () -> debateService.setStatus(testRoomId, debateRoomWithStateToUpdate));
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

        assertEquals(updatedRoom.getUser2().getId(), testUser.getId());
        assertEquals(updatedRoom.getUser2().getName(), testUser.getName());

    }

    @Test
    void addSecondParticipant_Guest_Success(){

        User testUser = new User();
        testUser.setId(null);

        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("test username 2");
        testUser2.setName("test user's name 2");
        testUser2.setCreationDate(LocalDate.parse("2019-01-21"));
        testUser2.setToken("lajflfa");

        User guestUser = new User();
        guestUser.setId(1L);
        guestUser.setUsername("Guest");
        guestUser.setName("Guest");
        guestUser.setCreationDate(LocalDate.parse("2019-01-21"));
        guestUser.setToken("lajflfa");

        DebateRoomPostDTO debateRoomPostDTO = new DebateRoomPostDTO();
        debateRoomPostDTO.setUserId(20L);
        debateRoomPostDTO.setDebateId(1L);
        debateRoomPostDTO.setSide(DebateSide.FOR);

        DebateRoom createdDebateRoom = debateService.createDebateRoom(testDebateRoom, debateRoomPostDTO);
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser2);
        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(createdDebateRoom);
        Mockito.when(userService.createGuestUser(Mockito.any())).thenReturn(guestUser);

        DebateRoom updatedRoom = debateService.addParticipantToRoom(createdDebateRoom, testUser);

        assertEquals(updatedRoom.getUser2().getId(), guestUser.getId());
        assertEquals(updatedRoom.getUser2().getName(), guestUser.getName());

    }

  @Test
  void getAllDebateRooms_Success(){
      DebateRoom debateRoom1 = new DebateRoom();
      debateRoom1.setRoomId(1L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ONE_USER_AGAINST);

      DebateRoom debateRoom2 = new DebateRoom();
      debateRoom1.setRoomId(2L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.READY_TO_START);

      DebateRoom debateRoom3 = new DebateRoom();
      debateRoom1.setRoomId(3L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ONGOING_FOR);

      DebateRoom debateRoom4 = new DebateRoom();
      debateRoom1.setRoomId(4L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ENDED);

      List<DebateRoom> debateRooms = new ArrayList<>();
      debateRooms.add(debateRoom1);
      debateRooms.add(debateRoom2);
      debateRooms.add(debateRoom3);
      debateRooms.add(debateRoom4);

      Mockito.when(debateRoomRepository.findAllByCreatorUserId(Mockito.any())).thenReturn(debateRooms);

      List<DebateRoom> retrievedDebateRooms = debateService.getDebateRoomsByUserId(creatingUser.getId(), null);

      assertEquals(retrievedDebateRooms.size(), debateRooms.size());
      assertEquals(retrievedDebateRooms.get(0), debateRooms.get(0));
      assertEquals(retrievedDebateRooms.get(1), debateRooms.get(1));
      assertEquals(retrievedDebateRooms.get(2), debateRooms.get(2));
      assertEquals(retrievedDebateRooms.get(3), debateRooms.get(3));

  }

    @Test
    void getAllDebateRooms_SpecificState(){
        DebateRoom debateRoom1 = new DebateRoom();
        debateRoom1.setRoomId(1L);
        debateRoom1.setCreatorUserId(creatingUser.getId());
        debateRoom1.setDebateState(DebateState.ONE_USER_AGAINST);

        DebateRoom debateRoom2 = new DebateRoom();
        debateRoom1.setRoomId(2L);
        debateRoom1.setCreatorUserId(creatingUser.getId());
        debateRoom1.setDebateState(DebateState.READY_TO_START);

        DebateRoom debateRoom3 = new DebateRoom();
        debateRoom1.setRoomId(3L);
        debateRoom1.setCreatorUserId(creatingUser.getId());
        debateRoom1.setDebateState(DebateState.ONGOING_FOR);

        DebateRoom debateRoom4 = new DebateRoom();
        debateRoom1.setRoomId(4L);
        debateRoom1.setCreatorUserId(creatingUser.getId());
        debateRoom1.setDebateState(DebateState.ENDED);

        List<DebateRoom> debateRooms = new ArrayList<>();
        debateRooms.add(debateRoom1);
        debateRooms.add(debateRoom2);
        debateRooms.add(debateRoom3);
        debateRooms.add(debateRoom4);

        List<DebateRoom> testDebateRoomList = debateRooms.subList(0,1);
        Mockito.when(debateRoomRepository.findAllByCreatorUserIdAndDebateState(Mockito.any(), Mockito.any()))
                .thenReturn(testDebateRoomList);

        List<DebateRoom> retrievedDebateRooms = debateService.getDebateRoomsByUserId(creatingUser.getId(), DebateState.ONE_USER_AGAINST);

        assertEquals(retrievedDebateRooms.size(), testDebateRoomList.size());
        assertEquals(retrievedDebateRooms.get(0), testDebateRoomList.get(0));

    }

}
