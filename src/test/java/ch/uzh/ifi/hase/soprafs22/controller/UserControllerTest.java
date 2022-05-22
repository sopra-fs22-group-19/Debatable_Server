package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;


  private User testUser;

  private UserPostDTO testUserPostDTO;

  private UserPutDTO testUserPutDTO;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  public void setup() {
      testUser = new User();
      testUser.setId(1L);
      testUser.setUsername("testUsername");
      testUser.setPassword("testPassword");
      testUser.setName("testName");
      testUser.setToken("1");
      testUser.setCreationDate(LocalDate.parse("2019-01-21"));

      testUserPostDTO = new UserPostDTO();
      testUserPostDTO.setUsername("testUsername");
      testUserPostDTO.setName("testName");
      testUserPostDTO.setPassword("testPassword");

      testUserPutDTO = new UserPutDTO();
      testUserPutDTO.setUserId(1L);
      testUserPutDTO.setUsername("testUsername");
      testUserPutDTO.setPassword("testPassword");
      testUserPutDTO.setName("testName");

  }


  @Test

  void createUser_validInput_userCreated() throws Exception {
    // given
    doReturn(testUser).when(userService).createUser(Mockito.any());

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(testUserPostDTO));

    // then
    mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
            .andExpect(jsonPath("$.username", is(testUser.getUsername())))
            .andExpect(jsonPath("$.name", is(testUser.getName())));
  }

  @Test
  void createUser_failed_usernameAlreadyExist() throws Exception {
      Exception eConflict = new ResponseStatusException(HttpStatus.CONFLICT);
      doThrow(eConflict).when(userService).createUser(Mockito.any());

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/register")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(testUserPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isConflict());
  }

  @Test
  void createGuest_validInput_guestCreated() throws Exception {
      User guestUser = new User();

      guestUser.setUsername(UUID.randomUUID().toString());
      guestUser.setName("Guest");
      guestUser.setPassword(UUID.randomUUID().toString());
      guestUser.setToken(UUID.randomUUID().toString());
      guestUser.setCreationDate(LocalDate.now());

      UserPostDTO guestUserPostDTO = new UserPostDTO();
      guestUserPostDTO.setName("Guest");
        // given
      doReturn(guestUser).when(userService).createGuestUser();

        // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/register/guests")
              .contentType(MediaType.APPLICATION_JSON);

        // then
      mockMvc.perform(postRequest)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.name", is(guestUserPostDTO.getName())));
  }

  @Test
  void deleteUser() throws Exception {

      doNothing().when(userService).deleteUser(Mockito.any());

      MockHttpServletRequestBuilder deleteRequest = delete("/users/1")
              .contentType(MediaType.APPLICATION_JSON);

      mockMvc.perform(deleteRequest)
              .andExpect(status().isOk());
  }

  @Test
  void updateUserCredentials() throws Exception {

      User testUser1 = new User();
      testUser1.setId(1L);
      testUser1.setUsername("testUsername1");
      testUser1.setPassword("testPassword1");
      testUser1.setName("testName1");

      Mockito.when(userService.getUserByUserId(Mockito.any(),Mockito.any())).thenReturn(testUser1);

      MockHttpServletRequestBuilder putRequest = put("/users/" + testUserPutDTO.getUserId())
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(testUserPutDTO));

      mockMvc.perform(putRequest)
              .andExpect(status().isOk());
  }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}