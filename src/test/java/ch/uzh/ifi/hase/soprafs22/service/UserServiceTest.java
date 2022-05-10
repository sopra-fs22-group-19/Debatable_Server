package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private DebateTopicRepository debateTopicRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testUsername");
    testUser.setPassword("testPassword");
    testUser.setName("testName");


    DebateTopic defaultDebateTopic1 =  new DebateTopic();
    defaultDebateTopic1.setCreatorUser(testUser);
    defaultDebateTopic1.setTopic("Default Topic 1 added by DebateService, TBD");
    defaultDebateTopic1.setTopicDescription("Default Topic 1 description, TBD");

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    Mockito.when(debateTopicRepository.saveAll(Mockito.any())).thenReturn(List.of(defaultDebateTopic1));
  }

  //us_01 test
  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertEquals(testUser.getName(), createdUser.getName());
    assertNotNull(createdUser.getCreationDate());
    assertNotNull(createdUser.getToken());

  }

  @Test
  void createUser_duplicateUsername_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  void checkUserCredentials_validUser(){
      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

      // given -> a first user has already been created
      User autenticatedUser = userService.checkCredentials(testUser.getUsername(), testUser.getPassword());

      assertEquals(autenticatedUser, testUser);
  }

  @Test
  void checkUserCredentials_userNotFound_throwsException(){
      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
      String testUserUsername = testUser.getUsername();
      String testUserPassword = testUser.getPassword();

      assertThrows(ResponseStatusException.class,
              () -> userService.checkCredentials(testUserUsername, testUserPassword));
  }

  @Test
  void checkUserCredentials_wrongPassword_throwsException(){
      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
      String testUserUsername = testUser.getUsername();

      assertThrows(ResponseStatusException.class,
              () -> userService.checkCredentials(testUserUsername, "wrong password"));
  }


  @Test
  void createGuestUser_validInputs_success() {
    User guestUser = new User();
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(guestUser);

    User createdGuestUser = userService.createGuestUser(guestUser);

    assertEquals("Guest", createdGuestUser.getName());
    assertNotNull(createdGuestUser.getCreationDate());
    assertNotNull(createdGuestUser.getToken());
  }

  @Test
  void deleteGuestUser(){
      // for given input
      Long id = 1L;
      // when the desired action performed
      userService.deleteUser(id);
      // then verify
      Mockito.verify(userRepository).deleteById(id);
  }

  @Test
  void updateCredentials_Successful(){

      User testUser1 = new User();
      testUser1.setId(1L);
      testUser1.setUsername("testUsername1");
      testUser1.setPassword("testPassword1");
      testUser1.setName("testName1");


      User testUser2 = new User();
      testUser2.setId(1L);
      testUser2.setUsername("testUsername2");
      testUser2.setPassword("testPassword2");
      testUser2.setName("testName2");

      Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser1);
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);


      User compareUer = userService.updateUser(testUser1.getId(), testUser2);

      assertEquals(compareUer.getName(), testUser2.getName());
      assertEquals(compareUer.getPassword(),testUser2.getPassword());
      assertEquals(compareUer.getUsername(), testUser2.getUsername());

  }

    @Test
    void updateCredentials_userNotFound_throwsException(){

        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");
        testUser1.setName("testName1");

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(null);


        assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(222222L , testUser1));

    }

    @Test
    void updateCredentials_samePassword_throwsException(){

        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword");
        testUser1.setName("testName1");

        User testUser2 = new User();
        testUser2.setId(1L);
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword");
        testUser2.setName("testName2");

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser1);

        assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(testUser1.getId() , testUser2));

    }

    @Test
    void updateCredentials_occupiedUsername_throwsException(){

        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("testUsername");
        testUser1.setPassword("testPassword1");
        testUser1.setName("testName1");

        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("testUsername");
        testUser2.setPassword("testPassword2");
        testUser2.setName("testName2");

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser2);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser1);

        assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(testUser2.getId() , testUser2));

    }

    @Test
    void updateCredentials_Successful_justName(){

        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");
        testUser1.setName("testName1");

        User testUser2 = new User();
        testUser2.setId(1L);
        testUser2.setName("testName2");

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser1);

        User compareUer = userService.updateUser(testUser2.getId(), testUser2);

        assertEquals(compareUer.getName(), testUser2.getName());
        assertEquals(compareUer.getPassword(),testUser1.getPassword());
        assertEquals(compareUer.getUsername(), testUser1.getUsername());

    }

    @Test
    void updateCredentials_Successful_justPassword(){

        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");
        testUser1.setName("testName1");

        User testUser2 = new User();
        testUser2.setId(1L);
        testUser2.setPassword("testPassword2");

        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser1);

        User compareUer = userService.updateUser(testUser2.getId(), testUser2);

        assertEquals(compareUer.getName(), testUser1.getName());
        assertEquals(compareUer.getPassword(),testUser2.getPassword());
        assertEquals(compareUer.getUsername(), testUser1.getUsername());

    }

}
