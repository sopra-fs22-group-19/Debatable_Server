package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateRoomRepository;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.TagRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DebateServiceTest {

  @Mock
  private DebateRoomRepository debateRoomRepository;

  @Mock
  private DebateTopicRepository debateTopicRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private TagRepository tagRepository;

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
    testDebateRoom.setSpeakers(new ArrayList<>());
    testDebateRoom.setUser1(debatesSpeaker1);


      // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(debateRoomRepository.save(Mockito.any())).thenReturn(testDebateRoom);
    Mockito.when(debateTopicRepository.findById(Mockito.any())).thenReturn(Optional.of(testDebateTopic));
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(creatingUser));
  }

  @Test
  public void createDebateRoom_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    DebateRoomPostDTO debateRoomPostDTO = new DebateRoomPostDTO();
    debateRoomPostDTO.setUserId(1L);
    debateRoomPostDTO.setDebateId(1L);
    debateRoomPostDTO.setSide("FOR");

    DebateRoom createdDebateRoom = debateService.createDebateRoom(testDebateRoom, debateRoomPostDTO);

    // then
    Mockito.verify(debateRoomRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testDebateRoom.getRoomId(), createdDebateRoom.getRoomId());
    assertEquals(testDebateRoom.getCreatorUserId(), createdDebateRoom.getCreatorUserId());
    assertEquals(testDebateRoom.getDebateRoomStatus(), createdDebateRoom.getDebateRoomStatus());
    assertEquals(testDebateRoom.getDebateTopic(), createdDebateRoom.getDebateTopic());
    assertEquals(testDebateRoom.getSpeakers(), createdDebateRoom.getSpeakers());
  }


}
