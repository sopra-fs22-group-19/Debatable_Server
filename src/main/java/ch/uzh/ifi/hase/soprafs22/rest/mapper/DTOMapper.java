package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.Intervention;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "token", ignore = true)
  @Mapping(source = "password", target = "password")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "userId")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "creationDate", target = "creation_date")
  @Mapping(source = "token", target = "token")
  UserGetDTO convertEntityToUserGetDTO(User user);

  // DTOs for the DebateTopic
  @Mapping(source = "debateTopicId", target = "debateId")
  @Mapping(source = "creatorUserId", target = "userId")
  @Mapping(source = "topic", target = "topic")
  @Mapping(source = "topicDescription", target = "description")
  @Mapping(source = "category", target = "category")
  DebateTopicGetDTO convertEntityToDebateGetDTO(DebateTopic debateTopic);
  
  // DTOs for the DebateRoom
  @Mapping(target = "roomId", ignore = true)
  @Mapping(target = "speakers", ignore = true)
  @Mapping(target = "debateTopic", ignore = true)
  @Mapping(target = "debateState", ignore = true)
  @Mapping(target = "debateStateUpdateTime", ignore = true)
  @Mapping(source = "userId", target = "creatorUserId")
  @Mapping(target = "interventions", ignore = true)
  @Mapping(target = "user1", ignore = true)
  @Mapping(target = "user2", ignore = true)
  @Mapping(target = "side1", ignore = true)
  @Mapping(target = "side2", ignore = true)
  DebateRoom convertDebateRoomPostDTOtoEntity(DebateRoomPostDTO debateRoomPostDTO);

  @Mapping(source = "roomId", target = "roomId")
  @Mapping(source = "debateTopic", target = "debate")
  @Mapping(source = "user1", target = "user1")
  @Mapping(source = "side1", target = "side1")
  @Mapping(source = "user2", target = "user2")
  @Mapping(source = "side2", target = "side2")
  @Mapping(source = "debateState", target = "debateStatus")
  DebateRoomGetDTO convertEntityToDebateRoomGetDTO(DebateRoom debateRoom);

  @Mapping(source = "debateState", target = "debateState")
  @Mapping(target = "debateStateUpdateTime", ignore = true)
  @Mapping(target = "speakers", ignore = true)
  @Mapping(target = "debateTopic", ignore = true)
  @Mapping(target = "roomId", ignore = true)
  @Mapping(target = "creatorUserId", ignore = true)
  @Mapping(target = "interventions", ignore = true)
  @Mapping(target = "user1", ignore = true)
  @Mapping(target = "user2", ignore = true)
  @Mapping(target = "side1", ignore = true)
  @Mapping(target = "side2", ignore = true)
  DebateRoom convertDebateRoomStatusPutDTOtoEntity(DebateRoomStatusPutDTO debateRoomStatusPutDTO);

  @Mapping(source = "userId", target = "id")
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "token", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "username", ignore = true)
  User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

  // DTOs for the Intervention
  @Mapping(target = "msgId", ignore = true)
  @Mapping(target = "postingSpeaker", ignore = true)
  @Mapping(target = "debateRoom", ignore = true)
  @Mapping(source = "messageContent", target = "message")
  @Mapping(target = "timestamp", ignore = true)
  Intervention convertInterventionPostDTOtoEntity(InterventionPostDTO interventionPostDTO);

  @Mapping(target = "debateTopicId", ignore = true)
  @Mapping(target = "creatorUser", ignore = true)
  @Mapping(target = "isDefaultTopic", ignore = true)
  @Mapping(source = "topic", target = "topic")
  @Mapping(source = "description", target = "topicDescription")
  @Mapping(source = "category", target = "category")
  DebateTopic convertDebateTopicPostDTOtoEntity(DebateTopicPostDTO debateTopicPostDTO);

}
