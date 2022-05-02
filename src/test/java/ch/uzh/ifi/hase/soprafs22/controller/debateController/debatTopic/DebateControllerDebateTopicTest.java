package ch.uzh.ifi.hase.soprafs22.controller.debateController.debatTopic;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.controller.DebateController;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateTopicGetDTO;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
class DebateControllerDebateTopicTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DebateService debateService;

  private DebateRoom debateRoom;

  private User creatingUser;

  @BeforeEach
  public void setup() {
    // Create first speaker (creating user)
    creatingUser = new User();
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
    debateTopic.setCreatorUser(creatingUser);
    debateTopic.setDebateTopicId(1L);
    debateTopic.setTopic("Topic 1");
    debateTopic.setTopicDescription("Topic 1' description");

    // Create reference DebateRoom
    debateRoom = new DebateRoom();
    debateRoom.setRoomId(1L);
    debateRoom.setCreatorUserId(1L);
    debateRoom.setDebateTopic(debateTopic);
    debateRoom.setSpeakers(speakerList);
    debateRoom.setDebateState(DebateState.ONE_USER_AGAINST);

  }

  @Test
  void getTopicByUser_validInput_debateTopicsReturned()throws Exception {

      DebateTopic defaultDebateTopic1 =  new DebateTopic();
      defaultDebateTopic1.setCreatorUser(creatingUser);
      defaultDebateTopic1.setTopic("Topic1");
      defaultDebateTopic1.setTopicDescription("Test default Topic1 description");

      DebateTopic defaultDebateTopic2 =  new DebateTopic();
      defaultDebateTopic2.setCreatorUser(creatingUser);
      defaultDebateTopic2.setTopic("Topic2");
      defaultDebateTopic2.setTopicDescription("Test default Topic2 description");

      List<DebateTopic> defaultDebateTopic= List.of(defaultDebateTopic1,defaultDebateTopic2);

      DebateTopicGetDTO debateTopicGetDTO1 = new DebateTopicGetDTO();
      debateTopicGetDTO1.setUserId(1L);
      debateTopicGetDTO1.setTopic("Topic1");
      debateTopicGetDTO1.setDescription("Test default Topic1 description");

      DebateTopicGetDTO debateTopicGetDTO2 = new DebateTopicGetDTO();
      debateTopicGetDTO2.setUserId(1L);
      debateTopicGetDTO2.setTopic("Topic2");
      debateTopicGetDTO2.setDescription("Test default Topic2 description");

      doReturn(defaultDebateTopic).when(debateService).getDebateTopicByUserId(1L);

      MockHttpServletRequestBuilder getRequest = get("/debates/1")
              .contentType(MediaType.APPLICATION_JSON);

      mockMvc.perform(getRequest).andExpect(status().isOk())
              .andExpect(jsonPath("$", hasSize(2)))
              .andExpect(jsonPath("$[0].topic", is(defaultDebateTopic1.getTopic())))
              .andExpect(jsonPath("$[0].description", is(defaultDebateTopic1.getTopicDescription())))
              .andExpect(jsonPath("$[1].topic", is(defaultDebateTopic2.getTopic())))
              .andExpect(jsonPath("$[1].description", is(defaultDebateTopic2.getTopicDescription())));

  }

  @Test
  void getTopicByUser_invalidUserId_UserNotFound()throws Exception {

      DebateTopic defaultDebateTopic1 =  new DebateTopic();
      defaultDebateTopic1.setCreatorUser(creatingUser);
      defaultDebateTopic1.setTopic("Topic1");
      defaultDebateTopic1.setTopicDescription("Test default Topic1 description");

      DebateTopic defaultDebateTopic2 =  new DebateTopic();
      defaultDebateTopic2.setCreatorUser(creatingUser);
      defaultDebateTopic2.setTopic("Topic2");
      defaultDebateTopic2.setTopicDescription("Test default Topic2 description");

      List<DebateTopic> defaultDebateTopic= List.of(defaultDebateTopic1,defaultDebateTopic2);


      DebateTopicGetDTO debateTopicGetDTO1 = new DebateTopicGetDTO();
      debateTopicGetDTO1.setUserId(1L);
      debateTopicGetDTO1.setTopic("Topic1");
      debateTopicGetDTO1.setDescription("Test default Topic1 description");

      DebateTopicGetDTO debateTopicGetDTO2 = new DebateTopicGetDTO();
      debateTopicGetDTO2.setUserId(1L);
      debateTopicGetDTO2.setTopic("Topic2");
      debateTopicGetDTO2.setDescription("Test default Topic2 description");

      Exception excNotFound = new ResponseStatusException(HttpStatus.NOT_FOUND);

      doThrow(excNotFound).when(debateService).getDebateTopicByUserId(2L);

      MockHttpServletRequestBuilder getRequest = get("/debates/2")
              .contentType(MediaType.APPLICATION_JSON);

      mockMvc.perform(getRequest).andExpect(status().isNotFound());


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