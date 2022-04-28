package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.Intervention;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.InterventionPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the DebateService REST resource.
 *
 * @see DebateService
 */
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class DebateServiceIntegrationTest {

    @Qualifier("debateTopicRepository")
    @Autowired
    private DebateTopicRepository debateTopicRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DebateService debateService;

    @Autowired
    private UserService userService;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        testUser1 = new User();
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword");
        testUser1.setName("testName");


        testUser2 = new User();
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword");
        testUser2.setName("testName");

        testUser1 = userService.createUser(testUser1);
        testUser2 = userService.createUser(testUser2);

    }

    @Test
    void createDebateRoomAndStartDebateAndPostBothSides(){
        // create debate room
        DebateRoomPostDTO debateRoomPostDTO = new DebateRoomPostDTO();
        debateRoomPostDTO.setUserId(testUser1.getId());
        debateRoomPostDTO.setDebateId(1L);
        debateRoomPostDTO.setSide(DebateSide.FOR);
        DebateRoom inputDebateRoom = DTOMapper.INSTANCE.convertDebateRoomPostDTOtoEntity(debateRoomPostDTO);

        DebateRoom createDebateRoom = debateService.createDebateRoom(inputDebateRoom, debateRoomPostDTO);
        assertNotNull(createDebateRoom);
        assertEquals(testUser1.getId(), createDebateRoom.getUser1().getId());
        assertEquals(debateRoomPostDTO.getSide(), createDebateRoom.getSide1());
        assertEquals(DebateState.ONE_USER_FOR, createDebateRoom.getDebateState());

        // Add the second user to the guest room
        createDebateRoom = debateService.addParticipantToRoom(createDebateRoom, testUser2);
        assertEquals(testUser2.getName(), createDebateRoom.getUser2().getName());
        assertEquals(DebateSide.AGAINST, createDebateRoom.getSide2());
        assertEquals(DebateState.READY_TO_START, createDebateRoom.getDebateState());

        // Start the debate
        DebateRoom dummyDebateRoom = new DebateRoom();
        dummyDebateRoom.setDebateState(DebateState.ONGOING_FOR);
        createDebateRoom = debateService.setStatus(createDebateRoom.getRoomId(), dummyDebateRoom);
        assertEquals(DebateState.ONGOING_FOR, createDebateRoom.getDebateState());

        // FOR user posts a message
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();
        interventionPostDTO.setRoomId(createDebateRoom.getRoomId());
        interventionPostDTO.setUserId(testUser1.getId());
        interventionPostDTO.setMessageContent("Message from user 1");
        Intervention inputIntervention = DTOMapper.INSTANCE.convertInterventionPostDTOtoEntity(interventionPostDTO);
        debateService.createIntervention(inputIntervention, interventionPostDTO);
        List<String> mesgsUser1 = debateService.getUserDebateInterventions(createDebateRoom.getRoomId(), testUser1.getId(), 1, 1);
        assertEquals(interventionPostDTO.getMessageContent(), mesgsUser1.get(0));

        // Check the turn changed
        createDebateRoom = debateService.getDebateRoom(createDebateRoom.getRoomId(), "");
        assertEquals(DebateState.ONGOING_AGAINST, createDebateRoom.getDebateState());

        // AGAINST user posts a message
        interventionPostDTO.setUserId(testUser2.getId());
        interventionPostDTO.setMessageContent("Message from user 2");
        inputIntervention = DTOMapper.INSTANCE.convertInterventionPostDTOtoEntity(interventionPostDTO);
        debateService.createIntervention(inputIntervention, interventionPostDTO);
        List<String> mesgsUser2 = debateService.getUserDebateInterventions(createDebateRoom.getRoomId(), testUser2.getId(), 1, 1);
        assertEquals(interventionPostDTO.getMessageContent(), mesgsUser2.get(0));

        // Check the turn changed
        createDebateRoom = debateService.getDebateRoom(createDebateRoom.getRoomId(), "");
        assertEquals(DebateState.ONGOING_FOR, createDebateRoom.getDebateState());

    }


  /* ignore integration test for now
  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setUsername("testUsername");

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setUsername("testUsername");
    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the name but forget about the username
    testUser2.setUsername("testUsername");

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

   */
}