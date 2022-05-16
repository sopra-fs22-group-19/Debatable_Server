package ch.uzh.ifi.hase.soprafs22.controller.debateController.intervention;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.controller.DebateController;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.InterventionPostDTO;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DebateController.class)
class DebateControllerInterventionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DebateService debateService;

    private DebateSpeaker debatesSpeaker1;
    private User testUser;

    private DebateRoom debateRoom;

    @BeforeEach
    public void setup() {
        // Create first speaker (creating user)
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test username");
        testUser.setName("test user's name");
        testUser.setCreationDate(LocalDate.parse("2019-01-21"));
        testUser.setToken("lajflfa");

        debatesSpeaker1 = new DebateSpeaker();
        debatesSpeaker1.setUserAssociated(testUser);
        debatesSpeaker1.setDebateSide(DebateSide.FOR);

        List<DebateSpeaker> speakerList = new ArrayList<>();
        speakerList.add(debatesSpeaker1);

        // Create Debate Topic
        DebateTopic debateTopic = new DebateTopic();
        debateTopic.setCreatorUser(testUser);
        debateTopic.setDebateTopicId(1L);
        debateTopic.setTopic("Topic 1");
        debateTopic.setTopicDescription("Topic 1' description");

        // Create refegetDebateRoomrence DebateRoom
        debateRoom = new DebateRoom();
        debateRoom.setRoomId(1L);
        debateRoom.setCreatorUserId(1L);
        debateRoom.setDebateTopic(debateTopic);
        debateRoom.setSpeakers(speakerList);
        debateRoom.setDebateState(DebateState.ONE_USER_AGAINST);

    }

    @Test
    void postMessage_validInput_return_void()throws Exception {
        Intervention inputIntervention = new Intervention();

        inputIntervention.setDebateRoom(debateRoom);

        inputIntervention.setPostingSpeaker(debatesSpeaker1);
        inputIntervention.setMessage("test_msg");
        inputIntervention.setMsgId(1L);
        inputIntervention.setTimestamp(LocalDateTime.now());

        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();
        interventionPostDTO.setRoomId(1L);
        interventionPostDTO.setUserId(1L);
        interventionPostDTO.setMessageContent("test_msg");


        doReturn(inputIntervention).when(debateService).createIntervention(Mockito.any(), Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/debates/rooms/1/msg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interventionPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isCreated());

    }

    @Test
    void postMessage_invalidUserId_return_notfound()throws Exception {
        Intervention inputIntervention = new Intervention();
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();
        interventionPostDTO.setRoomId(1L);
        interventionPostDTO.setUserId(1L);
        interventionPostDTO.setMessageContent("test_msg");

        Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);

        doThrow(eConflict).when(debateService).createIntervention(Mockito.any(),Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/debates/rooms/1/msg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interventionPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isNotFound());

    }

    @Test
    void postMessage_invalidRoomId_return_notfound()throws Exception {
        Intervention inputIntervention = new Intervention();
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();
        interventionPostDTO.setUserId(1L);
        interventionPostDTO.setMessageContent("test_msg");

        Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);

        doThrow(eConflict).when(debateService).createIntervention(Mockito.any(),Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/debates/rooms/1/msg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interventionPostDTO));

        mockMvc.perform(postRequest).andExpect(status().isNotFound());

    }

    @Test
    void getMessages_invalidRoomId_return_notfound()throws Exception {

        Exception eConflict = new ResponseStatusException(HttpStatus.NOT_FOUND);

        doThrow(eConflict).when(debateService).getUserDebateInterventions(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder postRequest = get("/debates/rooms/1/users/1/msgs")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest).andExpect(status().isNotFound());

    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }

}
