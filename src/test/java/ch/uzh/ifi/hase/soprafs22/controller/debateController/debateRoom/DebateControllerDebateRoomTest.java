package ch.uzh.ifi.hase.soprafs22.controller.debateController.debateRoom;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.controller.DebateController;
import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateSpeaker;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateRoomRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(DebateController.class)
class DebateControllerDebateRoomTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @Mock
  private DebateRoomRepository debateRoomRepository;

  @MockBean
  private DebateService debateService;

  private DebateRoom testDebateRoom;
  private DebateRoomPostDTO debateRoomPostDTO;

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
    testDebateRoom = new DebateRoom();
    testDebateRoom.setRoomId(1L);
    testDebateRoom.setCreatorUserId(1L);
    testDebateRoom.setDebateTopic(debateTopic);
    testDebateRoom.setSpeakers(speakerList);

    // Create reference DebateRoomPutDTO
    debateRoomPostDTO = new DebateRoomPostDTO();
    debateRoomPostDTO.setUserId(1L);
    debateRoomPostDTO.setDebateId(1L);
  }

  @Test
  void createDebateRoom_UserFOR_validInput_debateRoomCreated() throws Exception {
    // Check the end point returns the appropriate Debate Room object
    testDebateRoom.setDebateState(DebateState.ONE_USER_FOR);

    debateRoomPostDTO.setSide(DebateSide.FOR);

    given(debateService.createDebateRoom(Mockito.any(), Mockito.any())).willReturn(testDebateRoom);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/debates/rooms")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(debateRoomPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.roomId", is(testDebateRoom.getRoomId().intValue())))
        .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUserId().intValue())))
        .andExpect(jsonPath("$.debate.debateId", is(testDebateRoom.getDebateTopic().getDebateTopicId().intValue())))
        .andExpect(jsonPath("$.debate.topic", is(testDebateRoom.getDebateTopic().getTopic())))
        .andExpect(jsonPath("$.debate.description", is(testDebateRoom.getDebateTopic().getTopicDescription())))
        .andExpect(jsonPath("$.user1.userId", is(testDebateRoom.getUser1().getId().intValue())))
        .andExpect(jsonPath("$.user1.username", is(testDebateRoom.getUser1().getUsername())))
        .andExpect(jsonPath("$.user1.name", is(testDebateRoom.getUser1().getName())))
        .andExpect(jsonPath("$.user1.creation_date", is(testDebateRoom.getUser1().getCreationDate().toString())))
        .andExpect(jsonPath("$.side1", is(testDebateRoom.getSide1().name())))
        .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateState().name())));

  }

  @Test
  void createDebateRoom_UserAGAINST_validInput_debateRoomCreated() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      testDebateRoom.setDebateState(DebateState.ONE_USER_AGAINST);

      debateRoomPostDTO.setSide(DebateSide.AGAINST);

      given(debateService.createDebateRoom(Mockito.any(), Mockito.any()))
              .willReturn(testDebateRoom);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/debates/rooms")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(debateRoomPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.roomId", is(testDebateRoom.getRoomId().intValue())))
              .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUserId().intValue())))
              .andExpect(jsonPath("$.debate.debateId", is(testDebateRoom.getDebateTopic().getDebateTopicId().intValue())))
              .andExpect(jsonPath("$.debate.topic", is(testDebateRoom.getDebateTopic().getTopic())))
              .andExpect(jsonPath("$.debate.description", is(testDebateRoom.getDebateTopic().getTopicDescription())))
              .andExpect(jsonPath("$.user1.userId", is(testDebateRoom.getUser1().getId().intValue())))
              .andExpect(jsonPath("$.user1.username", is(testDebateRoom.getUser1().getUsername())))
              .andExpect(jsonPath("$.user1.name", is(testDebateRoom.getUser1().getName())))
              .andExpect(jsonPath("$.user1.creation_date", is(testDebateRoom.getUser1().getCreationDate().toString())))
              .andExpect(jsonPath("$.side1", is(testDebateRoom.getSide1().name())))
              .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateState().name())));

  }

  @Test
  void getDebateRoom_DebateRoomExists() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      given(debateService.getDebateRoom(Mockito.any())).willReturn(testDebateRoom);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = get("/debates/rooms/"+ testDebateRoom.getRoomId())
              .contentType(MediaType.APPLICATION_JSON);
      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.roomId", is(testDebateRoom.getRoomId().intValue())))
              .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUserId().intValue())))
              .andExpect(jsonPath("$.debate.debateId", is(testDebateRoom.getDebateTopic().getDebateTopicId().intValue())))
              .andExpect(jsonPath("$.debate.topic", is(testDebateRoom.getDebateTopic().getTopic())))
              .andExpect(jsonPath("$.debate.description", is(testDebateRoom.getDebateTopic().getTopicDescription())))
              .andExpect(jsonPath("$.user1.userId", is(testDebateRoom.getUser1().getId().intValue())))
              .andExpect(jsonPath("$.user1.username", is(testDebateRoom.getUser1().getUsername())))
              .andExpect(jsonPath("$.user1.name", is(testDebateRoom.getUser1().getName())))
              .andExpect(jsonPath("$.user1.creation_date", is(testDebateRoom.getUser1().getCreationDate().toString())))
              .andExpect(jsonPath("$.side1", is(testDebateRoom.getSide1().name())))
              .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateState().name())));
  }

  @Test
  void getDebateRoom_DebateRoomNotFound() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      given(debateService.getDebateRoom(Mockito.any())).willReturn(null);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = get("/debates/rooms/" + testDebateRoom.getRoomId())
              .contentType(MediaType.APPLICATION_JSON);

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isNotFound());
  }

  @Test
  void deleteDebateRoom_DebateRoomExists_Success() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      given(debateService.deleteRoom(Mockito.any())).willReturn(testDebateRoom);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = delete("/debates/rooms/"+ testDebateRoom.getRoomId())
              .contentType(MediaType.APPLICATION_JSON);
      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isOk());
  }

  @Test
  void deleteDebateRoom_DebateRoomNotFound() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);
      doThrow(eConflict).when(debateService).deleteRoom(Mockito.any());

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = get("/debates/rooms/" + testDebateRoom.getRoomId())
              .contentType(MediaType.APPLICATION_JSON);

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isNotFound());
  }

  @Test
  void addSecondParticipant_success() throws Exception {

      User testUser = new User();
      testUser.setId(2L);
      testUser.setUsername("test username 2");
      testUser.setName("test user's name 2");
      testUser.setCreationDate(LocalDate.parse("2019-01-21"));
      testUser.setToken("lajflfa");

      DebateSpeaker debateSpeaker = new DebateSpeaker();
      debateSpeaker.setUserAssociated(testUser);
      debateSpeaker.setDebateRoom(testDebateRoom);
      testDebateRoom.setUser2(debateSpeaker);
      testDebateRoom.setDebateRoomStatus(DebateState.READY_TO_START);

      UserPutDTO userPutDTO = new UserPutDTO();
      userPutDTO.setUserId(testDebateRoom.getRoomId());

      given(debateService.addParticipantToRoom(Mockito.any(), Mockito.any())).willReturn(testDebateRoom);

      MockHttpServletRequestBuilder putRequest = put("/debates/rooms/"+ testDebateRoom.getRoomId())
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPutDTO));
      // then
      mockMvc.perform(putRequest)
              .andExpect(status().isNoContent())
              .andExpect(jsonPath("$.roomId", is(testDebateRoom.getRoomId().intValue())))
              .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUserId().intValue())))
              .andExpect(jsonPath("$.debate.debateId", is(testDebateRoom.getDebateTopic().getDebateTopicId().intValue())))
              .andExpect(jsonPath("$.user1.userId", is(testDebateRoom.getUser1().getId().intValue())))
              .andExpect(jsonPath("$.user1.username", is(testDebateRoom.getUser1().getUsername())))
              .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateRoomStatus().name())))
              .andExpect(jsonPath("$.user2.userId", is(testUser.getId().intValue())))
              .andExpect(jsonPath("$.user2.username", is(testUser.getUsername())))
              .andExpect(jsonPath("user2.name", is(testUser.getName())));
  }

    @Test
    void addSecondParticipant_Guest_success() throws Exception {

        User guestUser = new User();
        guestUser.setId(20L);
        guestUser.setUsername("Guest");
        guestUser.setName("Guest");
        guestUser.setCreationDate(LocalDate.parse("2019-01-21"));
        guestUser.setToken("lajflfa");

        DebateSpeaker debateSpeaker = new DebateSpeaker();
        debateSpeaker.setUserAssociated(guestUser);
        debateSpeaker.setDebateRoom(testDebateRoom);
        testDebateRoom.setUser2(debateSpeaker);
        testDebateRoom.setDebateRoomStatus(DebateState.READY_TO_START);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUserId(null);

        given(debateService.addParticipantToRoom(Mockito.any(), Mockito.any())).willReturn(testDebateRoom);
        given(userService.createGuestUser(Mockito.any())).willReturn(guestUser);

        MockHttpServletRequestBuilder putRequest = put("/debates/rooms/"+ testDebateRoom.getRoomId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));
        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.roomId", is(testDebateRoom.getRoomId().intValue())))
                .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUserId().intValue())))
                .andExpect(jsonPath("$.debate.debateId", is(testDebateRoom.getDebateTopic().getDebateTopicId().intValue())))
                .andExpect(jsonPath("$.user1.userId", is(testDebateRoom.getUser1().getId().intValue())))
                .andExpect(jsonPath("$.user1.username", is(testDebateRoom.getUser1().getUsername())))
                .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateRoomStatus().name())))
                .andExpect(jsonPath("$.user2.userId", is(guestUser.getId().intValue())))
                .andExpect(jsonPath("$.user2.username", is(guestUser.getUsername())))
                .andExpect(jsonPath("user2.name", is(guestUser.getName())));

    }

    @Test
    void addSecondParticipant_RoomIdNotFound() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUserId(null);

        Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);
        doThrow(eConflict).when(debateService).addParticipantToRoom(Mockito.any(), Mockito.any());

        given(debateRoomRepository.findByRoomId(Mockito.any())).willReturn(null);

        MockHttpServletRequestBuilder putRequest = put("/debates/rooms/"+ -10)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());

    }

    @Test
    void setStatus_Success() throws Exception {

        int ongoing = 4;

        testDebateRoom.setDebateRoomStatus(DebateState.ONGOING);
        given(debateService.setStatus(Mockito.any(), Mockito.any())).willReturn(testDebateRoom);

        MockHttpServletRequestBuilder putRequest = put("/debates/status/"+ testDebateRoom.getRoomId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ongoing));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateRoomStatus().toString())));

    }

    @Test
    void setStatus_RoomNotFound() throws Exception {

        int ongoing = 4;

        Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);
        doThrow(eConflict).when(debateService).setStatus(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/debates/status/"+ testDebateRoom.getRoomId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ongoing));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void setStatus_InvalidStatus() throws Exception {

        int ongoing = 1000;

        Exception eConflict = new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        doThrow(eConflict).when(debateService).setStatus(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/debates/status/"+ testDebateRoom.getRoomId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ongoing));

        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized());
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