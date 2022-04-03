package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.DebateParticipantPosition;
import ch.uzh.ifi.hase.soprafs22.constant.DebateRoomStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(DebateController.class)
public class DebateControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DebateService debateService;

  private DebateRoom debateRoom;
  private DebateRoomPostDTO debateRoomPostDTO;

  @BeforeEach
  public void setup() {
    // Create reference DebateRoom
    debateRoom = new DebateRoom();
    debateRoom.setRoomId(1L);
    debateRoom.setCreatorUserId(1L);
    debateRoom.setDebateTopicId(1L);

    // Create reference DebateRoomPutDTO
    debateRoomPostDTO = new DebateRoomPostDTO();
    debateRoomPostDTO.setUserId(1L);
    debateRoomPostDTO.setDebateId(1L);
  }

  @Test
  public void createDebateRoom_UserFOR_validInput_debateRoomCreated() throws Exception {
    // Check the end point returns the appropriate Debate Room object
    debateRoom.setDebateRoomStatus(DebateRoomStatus.ONE_USER_FOR);

    debateRoomPostDTO.setSide(String.valueOf(DebateParticipantPosition.FOR));

    given(debateService.createDebateRoom(Mockito.any())).willReturn(debateRoom);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/debates/rooms")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(debateRoomPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.roomId", is(debateRoom.getRoomId().intValue())))
        .andExpect(jsonPath("$.debateStatus", is(is(debateRoom.getDebateRoomStatus().name()))));

  }

  @Test
  public void createDebateRoom_UserAGAINST_validInput_debateRoomCreated() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      debateRoom.setDebateRoomStatus(DebateRoomStatus.ONE_USER_AGAINST);

      debateRoomPostDTO.setSide(String.valueOf(DebateParticipantPosition.AGAINST));

      given(debateService.createDebateRoom(Mockito.any())).willReturn(debateRoom);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/debates/rooms")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(debateRoomPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.roomId", is(debateRoom.getRoomId().intValue())))
              .andExpect(jsonPath("$.debateStatus", is(is(debateRoom.getDebateRoomStatus().name()))));

  }

  @Test
  public void createDebateRoom_invalidInput_sideInvalid_debateRoomNotCreated() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      debateRoom.setDebateRoomStatus(DebateRoomStatus.ONE_USER_AGAINST);

      debateRoomPostDTO.setSide("lkasfjlaksdjgl");

      given(debateService.createDebateRoom(Mockito.any())).willReturn(debateRoom);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/debates/rooms")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(debateRoomPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isBadRequest());

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