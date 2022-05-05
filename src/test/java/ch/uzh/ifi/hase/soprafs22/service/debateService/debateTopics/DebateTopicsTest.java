package ch.uzh.ifi.hase.soprafs22.service.debateService.debateTopics;

import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateTopicPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class DebateTopicsTest {
  @Mock
  private DebateTopicRepository debateTopicRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserService userService;

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

      Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(creatingUser);

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

      Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(null);

      assertThrows(ResponseStatusException.class, () -> debateService.getDebateTopicByUserId(2L));

  }

  @Test
  void createDebateTopic_success(){
      DebateTopic expectedDebateTopic = new DebateTopic();
      expectedDebateTopic.setTopic("test topic");
      expectedDebateTopic.setTopicDescription("test topic description");
      expectedDebateTopic.setCreatorUser(creatingUser);

      Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(creatingUser);
      Mockito.when(userService.getUserByUserId(Mockito.any(), Mockito.any())).thenReturn(creatingUser);
      Mockito.when(debateTopicRepository.save(Mockito.any())).thenReturn(expectedDebateTopic);

      DebateTopic createdDebateTopic = debateService.createDebateTopic(creatingUser.getId(), expectedDebateTopic);

      assertEquals(createdDebateTopic.getTopic(), createdDebateTopic.getTopic());
      assertEquals(createdDebateTopic.getTopicDescription(), createdDebateTopic.getTopicDescription());
      assertEquals(createdDebateTopic.getCreatorUser(), createdDebateTopic.getCreatorUser());

  }

  @Test
  void createDebateTopic_UserNotFound_Fail(){
      DebateTopic expectedDebateTopic = new DebateTopic();
      expectedDebateTopic.setTopic("test topic");
      expectedDebateTopic.setTopicDescription("test topic description");
      expectedDebateTopic.setCreatorUser(creatingUser);

      Exception excNotFound = new ResponseStatusException(HttpStatus.NOT_FOUND);
      doThrow(excNotFound).when(userService).getUserByUserId(Mockito.any(), Mockito.any());

      assertThrows(ResponseStatusException.class, ()
              -> debateService.createDebateTopic(creatingUser.getId(), expectedDebateTopic));

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
