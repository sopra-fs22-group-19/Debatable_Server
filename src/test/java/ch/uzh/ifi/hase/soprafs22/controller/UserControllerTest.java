package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  private User testUser;

  private UserPostDTO testUserPostDTO;

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
  }


  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    doReturn(testUser).when(userService).createUser(Mockito.any());

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
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
      MockHttpServletRequestBuilder postRequest = post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(testUserPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isConflict());
  }

    @Test
    public void createGuest_validInput_guestCreated() throws Exception {
        User guestUser = new User();

        guestUser.setUsername(UUID.randomUUID().toString());
        guestUser.setName("Guest");
        guestUser.setPassword(UUID.randomUUID().toString());
        guestUser.setToken(UUID.randomUUID().toString());
        guestUser.setCreationDate(LocalDate.now());

        UserPostDTO guestUserPostDTO = new UserPostDTO();
        guestUserPostDTO.setName("Guest");
        // given
        doReturn(guestUser).when(userService).createGuestUser(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/guests")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(guestUserPostDTO.getName())));
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