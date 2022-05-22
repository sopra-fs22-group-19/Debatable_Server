package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.controller.DebateController;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private DebateController debateController;

    private User testUser;

    @BeforeEach
    public void setup() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");
        testUser.setCreationDate(LocalDate.now());
    }
    @Test
    public void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername(testUser.getUsername()));

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getCreationDate(), createdUser.getCreationDate());

        assertNotNull(createdUser.getToken());

        userRepository.deleteById(createdUser.getId());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername(testUser.getUsername()));

        // Create testUser
        User createdUser = userService.createUser(testUser);
        // attempt to create second user with same username
        User testUser2 = new User();
        testUser2.setUsername(testUser.getUsername());
        testUser2.setName("testName2");
        testUser2.setPassword("testPassword2");
        testUser2.setCreationDate(LocalDate.now());

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
        userRepository.deleteById(createdUser.getId());
    }

}

