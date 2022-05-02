package ch.uzh.ifi.hase.soprafs22.service.debateService.debateTopics;

import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class DebateTopicsTest {
  @Mock
  private DebateTopicRepository debateTopicRepository;

  @Mock
  private UserRepository userRepository;

  @Spy
  @InjectMocks
  private DebateService debateService;

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

      Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(creatingUser));

  }


  @Test
  void getDebateTopic_returnAllTopicByUser() {

      DebateTopic defaultDebateTopic1 =  new DebateTopic();
      defaultDebateTopic1.setCreatorUser(creatingUser);
      defaultDebateTopic1.setTopic("Test default Topic1 belongs user1");
      defaultDebateTopic1.setTopicDescription("Test default Topic1 description");

      DebateTopic defaultDebateTopic2 =  new DebateTopic();
      defaultDebateTopic2.setCreatorUser(creatingUser);
      defaultDebateTopic2.setTopic("Test default Topic2 belongs user2 ");
      defaultDebateTopic2.setTopicDescription("Test default Topic2 description");

      List<DebateTopic> testTopics = List.of(defaultDebateTopic1, defaultDebateTopic2);

      Mockito.when(debateTopicRepository.findByCreatorUser_Id(1L)).thenReturn(testTopics);


      List<DebateTopic> debateTopics = debateService.getDebateTopicByUserId(1L);

      assertEquals(testTopics.get(0).getCreatorUser(), debateTopics.get(0).getCreatorUser());
      assertEquals(testTopics.get(0).getTopic(), debateTopics.get(0).getTopic());
      assertEquals(testTopics.get(0).getTopicDescription(), debateTopics.get(0).getTopicDescription());
      assertEquals(testTopics.get(1).getCreatorUser(), debateTopics.get(1).getCreatorUser());
      assertEquals(testTopics.get(1).getTopic(), debateTopics.get(1).getTopic());
      assertEquals(testTopics.get(1).getTopicDescription(), debateTopics.get(1).getTopicDescription());
  }

  @Test
  void getDebateTopic_userIdNotFound_throwNotFound() {

      Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

      assertThrows(ResponseStatusException.class, () -> debateService.getDebateTopicByUserId(2L));

  }


  @Test
  void initializeDefaultTopics_Success_NoExceptionThrown() throws NoSuchMethodException {

      Method setupDefaultDebateTopics =  DebateService.class.getDeclaredMethod(
              "setupDefaultDebateTopics"); // methodName,parameters
      setupDefaultDebateTopics.setAccessible(true);

      Throwable throwable = catchThrowable(() -> setupDefaultDebateTopics.invoke(debateService));
      assertNull(throwable);
  }
}
