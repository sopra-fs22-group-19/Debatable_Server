package ch.uzh.ifi.hase.soprafs22.service.debateService.debateRoom;

import ch.uzh.ifi.hase.soprafs22.constant.DebateSide;
import ch.uzh.ifi.hase.soprafs22.constant.DebateState;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.repository.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.InterventionPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.DebateService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DebateServiceInterventionsTest {

    @Mock
    private DebateRoomRepository debateRoomRepository;

    @Mock
    private DebateTopicRepository debateTopicRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterventionRepository interventionRepository;

    @Mock
    private DebateSpeakerRepository debateSpeakerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private DebateService debateService;

    private DebateRoom testDebateRoom;
    private DebateSpeaker testDebateSpeakerFor;

    private DebateSpeaker testDebateSpeakerAgainst;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        User creatingUser = new User();
        creatingUser.setId(1L);
        creatingUser.setUsername("test username FOR");
        creatingUser.setName("test user's name FOR");
        creatingUser.setCreationDate(LocalDate.parse("2019-01-21"));
        creatingUser.setToken("lajflfa");

        testDebateSpeakerFor = new DebateSpeaker();
        testDebateSpeakerFor.setSpeakerId(1L);
        testDebateSpeakerFor.setUserAssociated(creatingUser);
        testDebateSpeakerFor.setDebateSide(DebateSide.FOR);

        User opposingUser = new User();
        opposingUser.setId(2L);
        opposingUser.setUsername("test username AGAINST");
        opposingUser.setName("test user's name AGAINST");
        opposingUser.setCreationDate(LocalDate.parse("2019-01-21"));
        opposingUser.setToken("lajflfa");

        testDebateSpeakerAgainst = new DebateSpeaker();
        testDebateSpeakerAgainst.setSpeakerId(2L);
        testDebateSpeakerAgainst.setUserAssociated(creatingUser);
        testDebateSpeakerAgainst.setDebateSide(DebateSide.AGAINST);

        DebateTopic testDebateTopic = new DebateTopic();
        testDebateTopic.setCreatorUserId(1L);
        testDebateTopic.setDebateTopicId(1L);
        testDebateTopic.setTopic("Topic 1");
        testDebateTopic.setTopicDescription("Topic 1' description");

        testDebateRoom = new DebateRoom();
        testDebateRoom.setRoomId(1L);
        testDebateRoom.setCreatorUserId(1L);
        testDebateRoom.setDebateTopic(testDebateTopic);
        testDebateRoom.setUser1(testDebateSpeakerFor);
        testDebateRoom.setUser1(testDebateSpeakerAgainst);


    }

    @Test
    void createIntervention_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        testDebateRoom.setDebateState(DebateState.ONGOING_FOR);
        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();
        interventionPostDTO.setRoomId(1L);
        interventionPostDTO.setUserId(1L);
        interventionPostDTO.setMessageContent("test_msg");

        Intervention inputIntervention = new Intervention();
        inputIntervention.setMessage("test_msg");

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test username");
        testUser.setName("test user's name");
        testUser.setCreationDate(LocalDate.parse("2019-01-21"));
        testUser.setToken("lajflfa");
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);

        DebateSpeaker testDebateSpeaker = new DebateSpeaker();
        testDebateSpeaker.setUserAssociated(testUser);
        testDebateSpeaker.setDebateSide(DebateSide.FOR);
        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeaker)));

        Intervention newIntervention = new Intervention();
        newIntervention.setMsgId(1L);
        newIntervention.setPostingSpeaker(testDebateSpeaker);
        newIntervention.setDebateRoom(testDebateRoom);
        newIntervention.setMessage("test_msg");
        newIntervention.setTimestamp(LocalDateTime.now());

        Mockito.when(interventionRepository.save(Mockito.any())).thenReturn(newIntervention);

        Intervention savedIntervention = debateService.createIntervention(inputIntervention, interventionPostDTO);

        assertEquals(interventionPostDTO.getMessageContent(), savedIntervention.getMessage());
        assertEquals(interventionPostDTO.getUserId(), savedIntervention.getPostingSpeaker().getUserAssociated().getId());
        assertEquals(interventionPostDTO.getRoomId(), savedIntervention.getDebateRoom().getRoomId());
    }

    @Test
    void createIntervention_NotSpeakersTurn() {
        // when -> any object is being save in the userRepository -> return the dummy
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();
        interventionPostDTO.setRoomId(1L);
        interventionPostDTO.setUserId(1L);
        interventionPostDTO.setMessageContent("test_msg");

        Intervention inputIntervention = new Intervention();
        inputIntervention.setMessage("test_msg");

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test username");
        testUser.setName("test user's name");
        testUser.setCreationDate(LocalDate.parse("2019-01-21"));
        testUser.setToken("lajflfa");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        DebateSpeaker testDebateSpeaker = new DebateSpeaker();
        testDebateSpeaker.setUserAssociated(testUser);

        Intervention newIntervention = new Intervention();
        newIntervention.setMsgId(1L);
        newIntervention.setPostingSpeaker(testDebateSpeaker);
        newIntervention.setDebateRoom(testDebateRoom);
        newIntervention.setMessage("test_msg");
        newIntervention.setTimestamp(LocalDateTime.now());

        Mockito.when(interventionRepository.save(Mockito.any())).thenReturn(newIntervention);

        testDebateSpeaker.setDebateSide(DebateSide.AGAINST);
        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeaker)));

        testDebateRoom.setDebateState(DebateState.ONGOING_FOR);
        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        assertThrows(ResponseStatusException.class,
                () -> debateService.createIntervention(inputIntervention, interventionPostDTO));

        testDebateSpeaker.setDebateSide(DebateSide.FOR);
        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeaker)));

        testDebateRoom.setDebateState(DebateState.ONGOING_AGAINST);
        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        assertThrows(ResponseStatusException.class,
                () -> debateService.createIntervention(inputIntervention, interventionPostDTO));

        assertThrows(ResponseStatusException.class,
                () -> debateService.createIntervention(inputIntervention, interventionPostDTO));
    }

    @Test
    void createIntervention_wrongRoomId_notfound() {
        // when -> setup additional mocks for UserRepository
        Intervention inputIntervention = new Intervention();
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class,
                () -> debateService.createIntervention(inputIntervention, interventionPostDTO));
    }

    @Test
    void createIntervention_wrongUserId_notfound() {
        // when -> setup additional mocks for UserRepository
        Intervention inputIntervention = new Intervention();
        InterventionPostDTO interventionPostDTO = new InterventionPostDTO();

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class,
                () -> debateService.createIntervention(inputIntervention, interventionPostDTO));
    }

    @Test
    void getInterventionsUser_getAllInterventions_Success() {
        int totalNumberInterventions = 3;

        // Create ordered interventions
        String baseString = "Debate Speaker: %d, Message #%d";
        List<String> expectedInterventionStringSpeaker1 = new ArrayList<>();
        List<Intervention> interventionListSpeaker1 = new ArrayList<>();
        List<String> expectedInterventionStringSpeaker2 = new ArrayList<>();
        List<Intervention> interventionListSpeaker2 = new ArrayList<>();
        int j = 0;
        for (DebateSpeaker debateSpeaker : Arrays.asList(testDebateSpeakerFor, testDebateSpeakerAgainst)) {
            for (int i = 0; i < totalNumberInterventions; i++) {
                Intervention intervention = new Intervention();
                intervention.setPostingSpeaker(testDebateSpeakerFor);
                intervention.setDebateRoom(testDebateRoom);
                intervention.setTimestamp(LocalDateTime.now());
                intervention.setMessage(String.format(baseString, debateSpeaker.getSpeakerId(), i));
                testDebateRoom.getInterventions().add(intervention);

                if (j == 0) {
                    interventionListSpeaker1.add(intervention);
                    expectedInterventionStringSpeaker1.add(intervention.getMessage());
                }
                else {
                    interventionListSpeaker2.add(intervention);
                    expectedInterventionStringSpeaker2.add(intervention.getMessage());
                }
            }
            j++;
        }

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);


        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerFor)));
        Mockito.when(interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(Mockito.any(), Mockito.any()))
                .thenReturn(interventionListSpeaker1);
        List<String> actualInterventionStringSpeaker1 = debateService.getUserDebateInterventions(
                testDebateRoom.getRoomId(), testDebateSpeakerFor.getUserAssociated().getId(), null, null);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerAgainst)));
        Mockito.when(interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(Mockito.any(), Mockito.any()))
                .thenReturn(interventionListSpeaker2);
        List<String> actualInterventionStringSpeaker2 = debateService.getUserDebateInterventions(
                testDebateRoom.getRoomId(), testDebateSpeakerAgainst.getUserAssociated().getId(), null, null);

        assertEquals(expectedInterventionStringSpeaker1, actualInterventionStringSpeaker1);
        assertEquals(expectedInterventionStringSpeaker2, actualInterventionStringSpeaker2);
    }

    @Test
    void getInterventionsUser_getTopIToTopJInterventions_InsideTotalInteventions_Success() {
        // Create ordered interventions

        int totalNumberInterventions = 10;
        int topI = 5;
        int toTopJ = 8;
        String baseString = "Debate Speaker: %d, Message #%d";
        List<String> expectedInterventionStringSpeaker1 = new ArrayList<>();
        List<Intervention> interventionListSpeaker1 = new ArrayList<>();
        List<String> expectedInterventionStringSpeaker2 = new ArrayList<>();
        List<Intervention> interventionListSpeaker2 = new ArrayList<>();

        int j = 0;
        for (DebateSpeaker debateSpeaker : Arrays.asList(testDebateSpeakerFor, testDebateSpeakerAgainst)) {
            for (int i = 0; i < totalNumberInterventions; i++) {
                Intervention intervention = new Intervention();
                intervention.setPostingSpeaker(testDebateSpeakerFor);
                intervention.setDebateRoom(testDebateRoom);
                intervention.setTimestamp(LocalDateTime.now());
                intervention.setMessage(String.format(baseString, debateSpeaker.getSpeakerId(), i));
                testDebateRoom.getInterventions().add(intervention);

                boolean addToExpectedIntevention = (totalNumberInterventions - topI >= i) && (i >= totalNumberInterventions - toTopJ);
                if (j == 0) {
                    interventionListSpeaker1.add(intervention);
                    if (addToExpectedIntevention)
                        expectedInterventionStringSpeaker1.add(intervention.getMessage());
                }
                else {
                    interventionListSpeaker2.add(intervention);
                    if (addToExpectedIntevention)
                        expectedInterventionStringSpeaker2.add(intervention.getMessage());
                }
            }
            j++;
        }

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerFor)));
        Mockito.when(interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(Mockito.any(), Mockito.any()))
                .thenReturn(interventionListSpeaker1);
        List<String> actualInterventionStringSpeaker1 = debateService.getUserDebateInterventions(
                testDebateRoom.getRoomId(), testDebateSpeakerFor.getUserAssociated().getId(), topI, toTopJ);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerAgainst)));
        Mockito.when(interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(Mockito.any(), Mockito.any()))
                .thenReturn(interventionListSpeaker2);
        List<String> actualInterventionStringSpeaker2 = debateService.getUserDebateInterventions(
                testDebateRoom.getRoomId(), testDebateSpeakerAgainst.getUserAssociated().getId(), topI, toTopJ);

        assertEquals(expectedInterventionStringSpeaker1, actualInterventionStringSpeaker1);
        assertEquals(expectedInterventionStringSpeaker2, actualInterventionStringSpeaker2);
    }


    @Test
    void getInterventionsUser_getTopIToTopJInterventions_ToTopJLargerTotalInteventions_Success() {
        // Create ordered interventions

        int totalNumberInterventions = 6;
        int topI = 5;
        int toTopJ = 8;
        String baseString = "Debate Speaker: %d, Message #%d";
        List<String> expectedInterventionStringSpeaker1 = new ArrayList<>();
        List<Intervention> interventionListSpeaker1 = new ArrayList<>();
        List<String> expectedInterventionStringSpeaker2 = new ArrayList<>();
        List<Intervention> interventionListSpeaker2 = new ArrayList<>();

        int j = 0;
        for (DebateSpeaker debateSpeaker : Arrays.asList(testDebateSpeakerFor, testDebateSpeakerAgainst)) {
            for (int i = 0; i < totalNumberInterventions; i++) {
                Intervention intervention = new Intervention();
                intervention.setPostingSpeaker(testDebateSpeakerFor);
                intervention.setDebateRoom(testDebateRoom);
                intervention.setTimestamp(LocalDateTime.now());
                intervention.setMessage(String.format(baseString, debateSpeaker.getSpeakerId(), i));
                testDebateRoom.getInterventions().add(intervention);

                boolean addToExpectedIntevention = (totalNumberInterventions - topI >= i);
                if (j == 0) {
                    interventionListSpeaker1.add(intervention);
                    if (addToExpectedIntevention)
                        expectedInterventionStringSpeaker1.add(intervention.getMessage());
                }
                else {
                    interventionListSpeaker2.add(intervention);
                    if (addToExpectedIntevention)
                        expectedInterventionStringSpeaker2.add(intervention.getMessage());
                }
            }
            j++;
        }

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerFor)));
        Mockito.when(interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(Mockito.any(), Mockito.any()))
                .thenReturn(interventionListSpeaker1);
        List<String> actualInterventionStringSpeaker1 = debateService.getUserDebateInterventions(
                testDebateRoom.getRoomId(), testDebateSpeakerFor.getUserAssociated().getId(), topI, toTopJ);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerAgainst)));
        Mockito.when(interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(Mockito.any(), Mockito.any()))
                .thenReturn(interventionListSpeaker2);
        List<String> actualInterventionStringSpeaker2 = debateService.getUserDebateInterventions(
                testDebateRoom.getRoomId(), testDebateSpeakerAgainst.getUserAssociated().getId(), topI, toTopJ);

        assertEquals(expectedInterventionStringSpeaker1, actualInterventionStringSpeaker1);
        assertEquals(expectedInterventionStringSpeaker2, actualInterventionStringSpeaker2);
    }

    @Test
    void getInterventionsUser_getTopNInterventions_TopILargerThanTotalInteventions_Success() {
        // Create ordered interventions

        int totalNumberInterventions = 4;
        int topI = 5;
        int toTopJ = 8;
        String baseString = "Debate Speaker: %d, Message #%d";
        List<String> expectedInterventionStringSpeaker1 = new ArrayList<>();
        List<Intervention> interventionListSpeaker1 = new ArrayList<>();
        List<String> expectedInterventionStringSpeaker2 = new ArrayList<>();
        List<Intervention> interventionListSpeaker2 = new ArrayList<>();

        int j = 0;
        for (DebateSpeaker debateSpeaker : Arrays.asList(testDebateSpeakerFor, testDebateSpeakerAgainst)) {
            for (int i = 0; i < totalNumberInterventions; i++) {
                Intervention intervention = new Intervention();
                intervention.setPostingSpeaker(testDebateSpeakerFor);
                intervention.setDebateRoom(testDebateRoom);
                intervention.setTimestamp(LocalDateTime.now());
                intervention.setMessage(String.format(baseString, debateSpeaker.getSpeakerId(), i));
                testDebateRoom.getInterventions().add(intervention);

                if (j == 0) {
                    interventionListSpeaker1.add(intervention);
                }
                else {
                    interventionListSpeaker2.add(intervention);
                }
            }
            j++;
        }

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerFor)));
        Mockito.when(interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(Mockito.any(), Mockito.any()))
                .thenReturn(interventionListSpeaker1);
        List<String> actualInterventionStringSpeaker1 = debateService.getUserDebateInterventions(
                testDebateRoom.getRoomId(), testDebateSpeakerFor.getUserAssociated().getId(), topI, toTopJ);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerAgainst)));
        Mockito.when(interventionRepository.findAllByDebateRoomRoomIdAndPostingSpeakerSpeakerIdOrderByTimestamp(Mockito.any(), Mockito.any()))
                .thenReturn(interventionListSpeaker2);
        List<String> actualInterventionStringSpeaker2 = debateService.getUserDebateInterventions(
                testDebateRoom.getRoomId(), testDebateSpeakerAgainst.getUserAssociated().getId(), topI, toTopJ);

        assertEquals(expectedInterventionStringSpeaker1, actualInterventionStringSpeaker1);
        assertEquals(expectedInterventionStringSpeaker2, actualInterventionStringSpeaker2);
    }

    @Test
    void getInterventionsUser_DebateRoomNotFound_Fail() {
        Long roomId = testDebateRoom.getRoomId();
        Long userId = testDebateSpeakerFor.getUserAssociated().getId();
        Integer topI = null;
        Integer toTopJ = null;

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class,
                () -> debateService.getUserDebateInterventions(roomId, userId, topI, toTopJ));
    }

    @Test
    void getInterventionsUser_UserNotFound_Fail() {
        Long roomId = testDebateRoom.getRoomId();
        Long userId = testDebateSpeakerFor.getUserAssociated().getId();
        Integer topI = null;
        Integer toTopJ = null;

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class,
                () -> debateService.getUserDebateInterventions(roomId, userId, topI, toTopJ));

    }

    @Test
    void getInterventionsUser_TopIEXORToTopJNull_Fail() {
        Long roomId = testDebateRoom.getRoomId();
        Long userId = testDebateSpeakerFor.getUserAssociated().getId();

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerFor)));

        Integer topI_1 = null;
        Integer toTopJ_1 = 4;
        assertThrows(ResponseStatusException.class,
                () -> debateService.getUserDebateInterventions(roomId, userId, topI_1, toTopJ_1));

        Integer topI_2 = 1;
        Integer toTopJ_2 = null;
        assertThrows(ResponseStatusException.class,
                () -> debateService.getUserDebateInterventions(roomId, userId, topI_2, toTopJ_2));
    }

    @Test
    void getInterventionsUser_ToTopJLargerThanTopINull_Fail() {
        Long roomId = testDebateRoom.getRoomId();
        Long userId = testDebateSpeakerFor.getUserAssociated().getId();

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerFor)));

        Integer topI = 10;
        Integer toTopJ = 1;
        assertThrows(ResponseStatusException.class,
                () -> debateService.getUserDebateInterventions(roomId, userId, topI, toTopJ));

    }

    @Test
    void getInterventionsUser_TopILessThanOne_Fail() {
        Long roomId = testDebateRoom.getRoomId();
        Long userId = testDebateSpeakerFor.getUserAssociated().getId();

        Mockito.when(debateRoomRepository.findByRoomId(Mockito.any())).thenReturn(testDebateRoom);

        Mockito.when(debateSpeakerRepository.findAllByUserAssociatedIdAndDebateRoomRoomId(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>(List.of(testDebateSpeakerFor)));

        Integer topI = 0;
        Integer toTopJ = 4;
        assertThrows(ResponseStatusException.class,
                () -> debateService.getUserDebateInterventions(roomId, userId, topI, toTopJ));
    }
}