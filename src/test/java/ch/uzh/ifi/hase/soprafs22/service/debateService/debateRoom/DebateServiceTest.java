package ch.uzh.ifi.hase.soprafs22.service.debateService.debateRoom;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateRoomRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DebateServiceTest {

  @Mock
  private DebateRoomRepository debateRoomRepository;

  @Mock
  private DebateTopicRepository debateTopicRepository;

  @Mock
  private UserRepository userRepository;

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
    testDebateRoom.setDebateRoomStatus(DebateState.NOT_STARTED);
    testDebateRoom.setDebateTopic(testDebateTopic);
    testDebateRoom.setUser1(debatesSpeaker1);


      // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(debateRoomRepository.save(Mockito.any())).thenReturn(testDebateRoom);
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
  void createRoom_defaultTopic(){
      testDebateRoom = new DebateRoom();
      testDebateRoom.setCreatorUserId(1L);

      // The assumption is the id 1 belongs to a default debate topic
      DebateRoomPostDTO testDebateRoomPostDTO = new DebateRoomPostDTO();
      testDebateRoomPostDTO.setUserId(1L);
      testDebateRoomPostDTO.setDebateId(-1L);
      testDebateRoomPostDTO.setSide(DebateSide.FOR);

      // testDebateRoom.setRoomId(1L);
      // has to have this debate stauts testDebateRoom.setDebateRoomStatus(DebateState.NOT_STARTED);
      // debateTopic has to match the id given of the room, user type (-1L)
      // setCreatorUserId has to match id of User1


      DebateRoom createdDebateRoom = debateService.createDebateRoom(testDebateRoom, testDebateRoomPostDTO);

      assertNotNull(createdDebateRoom.getRoomId());
      assertEquals(testDebateRoom.getCreatorUserId(), createdDebateRoom.getCreatorUserId());
      assertEquals(DebateState.ONE_USER_FOR, createdDebateRoom.getDebateRoomStatus());
      assertEquals(testDebateRoomPostDTO.getDebateId(), createdDebateRoom.getDebateTopic().getDebateTopicId());



  }

}
