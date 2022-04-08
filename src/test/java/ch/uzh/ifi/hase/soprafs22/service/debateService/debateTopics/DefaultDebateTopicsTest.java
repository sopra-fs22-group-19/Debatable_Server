package ch.uzh.ifi.hase.soprafs22.service.debateService.debateTopics;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;

class DefaultDebateTopicsTest {
  @Mock
  private DebateTopicRepository debateTopicRepository;

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

  }

  @Test
  public void DefaultTopicListExistsTest(){
      // TODO: Complete once method to get topic list is implemented
      return;
  }
}
