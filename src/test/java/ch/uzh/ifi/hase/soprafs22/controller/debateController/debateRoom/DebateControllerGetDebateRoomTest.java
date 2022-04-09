package ch.uzh.ifi.hase.soprafs22.controller.debateController.debateRoom;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.controller.DebateController;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(DebateController.class)
class DebateControllerGetDebateRoomTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DebateService debateService;

  private DebateRoom debateRoom;

  @BeforeEach
  public void setup() {
    // Create first speaker (creating user)
    User creatingUser = new User();
    creatingUser.setId(1L);
    creatingUser.setUsername("test username");
    creatingUser.setName("test user's name");
    creatingUser.setCreationDate(LocalDate.parse("2019-01-21"));
    creatingUser.setToken("lajflfa");

    DebateSpeaker debatesSpeaker1 = new DebateSpeaker();
    debatesSpeaker1.setUserAssociated(creatingUser);
    debatesSpeaker1.setDebateSide(DebateSide.FOR);

    List<DebateSpeaker> speakerList = new ArrayList<>();
    speakerList.add(debatesSpeaker1);

    // Create Debate Topic
    DebateTopic debateTopic = new DebateTopic();
    debateTopic.setCreatorUserId(1L);
    debateTopic.setDebateTopicId(1L);
    debateTopic.setTopic("Topic 1");
    debateTopic.setTopicDescription("Topic 1' description");

    // Create reference DebateRoom
    debateRoom = new DebateRoom();
    debateRoom.setRoomId(1L);
    debateRoom.setCreatorUserId(1L);
    debateRoom.setDebateTopic(debateTopic);
    debateRoom.setSpeakers(speakerList);
    debateRoom.setDebateRoomStatus(DebateState.ONE_USER_AGAINST);

  }

  @Test
  void getDebateRoom_DebateRoomExists() throws Exception {
    // Check the end point returns the appropriate Debate Room object
    given(debateService.getDebateRoom(Mockito.any())).willReturn(Optional.ofNullable(debateRoom));

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = get("/debates/rooms/"+ debateRoom.getRoomId())
        .contentType(MediaType.APPLICATION_JSON);
    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.roomId", is(debateRoom.getRoomId().intValue())))
        .andExpect(jsonPath("$.debate.userId", is(debateRoom.getDebateTopic().getCreatorUserId().intValue())))
        .andExpect(jsonPath("$.debate.debateId", is(debateRoom.getDebateTopic().getDebateTopicId().intValue())))
        .andExpect(jsonPath("$.debate.topic", is(debateRoom.getDebateTopic().getTopic())))
        .andExpect(jsonPath("$.debate.description", is(debateRoom.getDebateTopic().getTopicDescription())))
        .andExpect(jsonPath("$.user1.userId", is(debateRoom.getUser1().getId().intValue())))
        .andExpect(jsonPath("$.user1.username", is(debateRoom.getUser1().getUsername())))
        .andExpect(jsonPath("$.user1.name", is(debateRoom.getUser1().getName())))
        .andExpect(jsonPath("$.user1.creation_date", is(debateRoom.getUser1().getCreationDate().toString())))
        .andExpect(jsonPath("$.side1", is(debateRoom.getSide1().name())))
        .andExpect(jsonPath("$.debateStatus", is(debateRoom.getDebateRoomStatus().name())));
  }

  @Test
  void getDebateRoom_DebateRoomNotFound() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      given(debateService.getDebateRoom(Mockito.any())).willReturn(Optional.empty());

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = get("/debates/rooms/" + debateRoom.getRoomId())
              .contentType(MediaType.APPLICATION_JSON);

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isNotFound());
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