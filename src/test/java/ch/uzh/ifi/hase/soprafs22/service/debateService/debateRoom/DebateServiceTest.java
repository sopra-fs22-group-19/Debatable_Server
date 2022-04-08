package ch.uzh.ifi.hase.soprafs22.service.debateService.debateRoom;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateRoomRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DebateSpeakerRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

class DebateServiceTest {

  @Mock
  private DebateRoomRepository debateRoomRepository;

  @Mock
  private DebateTopicRepository debateTopicRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private DebateSpeakerRepository debateSpeakerRepository;

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
    void getDebateTopic_returnAllTopicByUser() {

        DebateTopic defaultDebateTopic1 =  new DebateTopic();
        defaultDebateTopic1.setCreatorUserId(1L);
        defaultDebateTopic1.setTopic("Test default Topic1 belongs user1");
        defaultDebateTopic1.setTopicDescription("Test default Topic1 description");

        DebateTopic defaultDebateTopic2 =  new DebateTopic();
        defaultDebateTopic2.setCreatorUserId(1L);
        defaultDebateTopic2.setTopic("Test default Topic2 belongs user2 ");
        defaultDebateTopic2.setTopicDescription("Test default Topic2 description");

        List<DebateTopic> testTopics = List.of(defaultDebateTopic1,defaultDebateTopic2);

        Mockito.when(debateTopicRepository.findByCreatorUserId(1L)).thenReturn(testTopics);

        List<DebateTopic> debateTopics = debateService.getDebateTopicByUserId(1L);

        assertEquals(testTopics.get(0).getCreatorUserId(), debateTopics.get(0).getCreatorUserId());
        assertEquals(testTopics.get(0).getTopic(), debateTopics.get(0).getTopic());
        assertEquals(testTopics.get(0).getTopicDescription(), debateTopics.get(0).getTopicDescription());
        assertEquals(testTopics.get(1).getCreatorUserId(), debateTopics.get(1).getCreatorUserId());
        assertEquals(testTopics.get(1).getTopic(), debateTopics.get(1).getTopic());
        assertEquals(testTopics.get(1).getTopicDescription(), debateTopics.get(1).getTopicDescription());

    }

    @Test
    void getDebateTopic_invalidId_throwNotFound() {

        DebateTopic defaultDebateTopic1 =  new DebateTopic();
        defaultDebateTopic1.setCreatorUserId(1L);
        defaultDebateTopic1.setTopic("Test default Topic1 belongs user1");
        defaultDebateTopic1.setTopicDescription("Test default Topic1 description");

        List<DebateTopic> testTopics = List.of(defaultDebateTopic1);

        Exception excConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);
        doThrow(excConflict).when(userRepository).findById(2L);

        assertThrows(ResponseStatusException.class, () -> debateService.getDebateTopicByUserId(2L));

    }


}
