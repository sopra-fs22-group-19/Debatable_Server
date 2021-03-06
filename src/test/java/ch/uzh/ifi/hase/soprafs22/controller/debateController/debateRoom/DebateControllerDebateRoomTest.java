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
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomStatusPutDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
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

  @MockBean
  private UserService userService;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Mock
  private DebateRoomRepository debateRoomRepository;

  @MockBean
  private DebateService debateService;

  private DebateRoom testDebateRoom;
  private DebateRoomPostDTO debateRoomPostDTO;

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
        .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUser().getId().intValue())))
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
              .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUser().getId().intValue())))
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
  void createDebateRoom_UserNotOrDebateTopicNotFound() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      testDebateRoom.setDebateState(DebateState.ONE_USER_AGAINST);
      debateRoomPostDTO.setSide(DebateSide.AGAINST);

      Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);
      doThrow(eConflict).when(debateService).createDebateRoom(Mockito.any(), Mockito.any());

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/debates/rooms/")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(debateRoomPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isNotFound());
  }

  @Test
  void getDebateRoom_DebateRoomExists() throws Exception {
      // Check the end point returns the appropriate Debate Room object
      given(debateService.getDebateRoom(Mockito.any(), Mockito.any())).willReturn(testDebateRoom);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = get("/debates/rooms/"+ testDebateRoom.getRoomId())
              .contentType(MediaType.APPLICATION_JSON);
      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.roomId", is(testDebateRoom.getRoomId().intValue())))
              .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUser().getId().intValue())))
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
      Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);
      doThrow(eConflict).when(debateService).getDebateRoom(Mockito.any(), Mockito.any());

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
      MockHttpServletRequestBuilder postRequest = delete("/debates/rooms/" + testDebateRoom.getRoomId())
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
      testDebateRoom.setDebateState(DebateState.READY_TO_START);

      UserPutDTO userPutDTO = new UserPutDTO();
      userPutDTO.setUserId(testDebateRoom.getRoomId());

      given(debateService.addParticipantToRoom(Mockito.any(), Mockito.any())).willReturn(testDebateRoom);

      MockHttpServletRequestBuilder putRequest = put("/debates/rooms/"+ testDebateRoom.getRoomId())
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPutDTO));
      // then
      mockMvc.perform(putRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.roomId", is(testDebateRoom.getRoomId().intValue())))
              .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUser().getId().intValue())))
              .andExpect(jsonPath("$.debate.debateId", is(testDebateRoom.getDebateTopic().getDebateTopicId().intValue())))
              .andExpect(jsonPath("$.user1.userId", is(testDebateRoom.getUser1().getId().intValue())))
              .andExpect(jsonPath("$.user1.username", is(testDebateRoom.getUser1().getUsername())))
              .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateState().name())))
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
        testDebateRoom.setDebateState(DebateState.READY_TO_START);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUserId(null);

        given(debateService.addParticipantToRoom(Mockito.any(), Mockito.any())).willReturn(testDebateRoom);
        given(userService.createGuestUser()).willReturn(guestUser);

        MockHttpServletRequestBuilder putRequest = put("/debates/rooms/"+ testDebateRoom.getRoomId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));
        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId", is(testDebateRoom.getRoomId().intValue())))
                .andExpect(jsonPath("$.debate.userId", is(testDebateRoom.getDebateTopic().getCreatorUser().getId().intValue())))
                .andExpect(jsonPath("$.debate.debateId", is(testDebateRoom.getDebateTopic().getDebateTopicId().intValue())))
                .andExpect(jsonPath("$.user1.userId", is(testDebateRoom.getUser1().getId().intValue())))
                .andExpect(jsonPath("$.user1.username", is(testDebateRoom.getUser1().getUsername())))
                .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateState().name())))
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
  void setStatus_StartDebate_Success() throws Exception {

        DebateRoomStatusPutDTO debateRoomStatusPutDTO = new DebateRoomStatusPutDTO();
        debateRoomStatusPutDTO.setDebateState(DebateState.ONGOING_FOR);

        testDebateRoom.setDebateState(DebateState.READY_TO_START);
        given(debateService.setStatus(Mockito.any(), Mockito.any())).willReturn(testDebateRoom);

        MockHttpServletRequestBuilder putRequest = put("/debates/rooms/"+ testDebateRoom.getRoomId() + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(debateRoomStatusPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.debateStatus", is(testDebateRoom.getDebateState().toString())));
  }

  @Test
  void setStatus_RoomNotFound() throws Exception {

        DebateRoomStatusPutDTO debateRoomStatusPutDTO = new DebateRoomStatusPutDTO();
        debateRoomStatusPutDTO.setDebateState(DebateState.ONGOING_FOR);

        Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);
        doThrow(eConflict).when(debateService).setStatus(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/debates/rooms/"+ testDebateRoom.getRoomId() + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(debateRoomStatusPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
  }

  @Test
  void setStatus_StartDebate_WrongStateNotAllowed() throws Exception {

      testDebateRoom.setDebateState(DebateState.ONE_USER_FOR);

      DebateRoomStatusPutDTO debateRoomStatusPutDTO = new DebateRoomStatusPutDTO();
      debateRoomStatusPutDTO.setDebateState(DebateState.ONGOING_FOR);

      Exception eConflict = new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
      doThrow(eConflict).when(debateService).setStatus(Mockito.any(), Mockito.any());

      MockHttpServletRequestBuilder putRequest = put("/debates/rooms/"+ testDebateRoom.getRoomId() + "/status")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(debateRoomStatusPutDTO));

      mockMvc.perform(putRequest)
              .andExpect(status().isMethodNotAllowed());
  }

  @Test
  void getDebateRoomsOfASpecificUser_Success() throws Exception {
      DebateRoom debateRoom1 = new DebateRoom();
      debateRoom1.setRoomId(1L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ONE_USER_AGAINST);

      DebateRoom debateRoom2 = new DebateRoom();
      debateRoom1.setRoomId(2L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.READY_TO_START);

      DebateRoom debateRoom3 = new DebateRoom();
      debateRoom1.setRoomId(3L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ONGOING_FOR);

      DebateRoom debateRoom4 = new DebateRoom();
      debateRoom1.setRoomId(4L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ENDED);

      List<DebateRoom> debateRooms = new ArrayList<>();
      debateRooms.add(debateRoom1);
      debateRooms.add(debateRoom2);
      debateRooms.add(debateRoom3);
      debateRooms.add(debateRoom4);

      given(debateService.getDebateRoomsByUserId(Mockito.any(), Mockito.any())).willReturn(debateRooms);

      MockHttpServletRequestBuilder getRequest = get("/debates/"+ creatingUser.getId() + "/rooms")
              .contentType(MediaType.APPLICATION_JSON);
      // then
      mockMvc.perform(getRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$[0].debateStatus", is(debateRooms.get(0).getDebateState().toString())))
              .andExpect(jsonPath("$[1].debateStatus", is(debateRooms.get(1).getDebateState().toString())))
              .andExpect(jsonPath("$[2].debateStatus", is(debateRooms.get(2).getDebateState().toString())))
              .andExpect(jsonPath("$[3].debateStatus", is(debateRooms.get(3).getDebateState().toString())));
  }

  @Test
  void getDebateRoomsOfASpecificUser_SpecificState_Success() throws Exception {
      DebateRoom debateRoom1 = new DebateRoom();
      debateRoom1.setRoomId(1L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ONE_USER_AGAINST);

      DebateRoom debateRoom2 = new DebateRoom();
      debateRoom1.setRoomId(2L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.READY_TO_START);

      DebateRoom debateRoom3 = new DebateRoom();
      debateRoom1.setRoomId(3L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ONGOING_FOR);

      DebateRoom debateRoom4 = new DebateRoom();
      debateRoom1.setRoomId(4L);
      debateRoom1.setCreatorUserId(creatingUser.getId());
      debateRoom1.setDebateState(DebateState.ENDED);

      List<DebateRoom> debateRooms = new ArrayList<>();
      debateRooms.add(debateRoom1);
      debateRooms.add(debateRoom2);
      debateRooms.add(debateRoom3);
      debateRooms.add(debateRoom4);

      given(debateService.getDebateRoomsByUserId(Mockito.any(), Mockito.any())).willReturn(debateRooms.subList(0, 1));

      MockHttpServletRequestBuilder getRequest = get("/debates/"+ creatingUser.getId() + "/rooms?state=ONE_USER_AGAINST")
              .contentType(MediaType.APPLICATION_JSON);
        // then
      mockMvc.perform(getRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$[0].debateStatus", is(debateRooms.get(0).getDebateState().toString())));

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