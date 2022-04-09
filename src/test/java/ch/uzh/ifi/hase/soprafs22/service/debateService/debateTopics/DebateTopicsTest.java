package ch.uzh.ifi.hase.soprafs22.service.debateService.debateTopics;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

class DebateTopicsTest {
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
      testDebateRoom.setSpeakers(new ArrayList<>());
      testDebateRoom.setUser1(debatesSpeaker1);

      Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(creatingUser));

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

      List<DebateTopic> testTopics = List.of(defaultDebateTopic1, defaultDebateTopic2);

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
