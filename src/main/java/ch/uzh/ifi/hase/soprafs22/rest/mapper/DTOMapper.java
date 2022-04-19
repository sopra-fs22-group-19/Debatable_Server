package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
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
  DebateTopicGetDTO convertEntityToDebateGetDTO(DebateTopic debateTopic);
  
  // DTOs for the DebateRoom
  @Mapping(target = "roomId", ignore = true)
  @Mapping(target = "speakers", ignore = true)
  @Mapping(target = "debateTopic", ignore = true)
  @Mapping(target = "debateRoomStatus", ignore = true)
  @Mapping(source = "userId", target = "creatorUserId")
  DebateRoom convertDebateRoomPostDTOtoEntity(DebateRoomPostDTO debateRoomPostDTO);

  @Mapping(source = "roomId", target = "roomId")
  @Mapping(source = "debateTopic", target = "debate")
  @Mapping(source = "user1", target = "user1")
  @Mapping(source = "side1", target = "side1")
  @Mapping(source = "user2", target = "user2")
  @Mapping(source = "side2", target = "side2")
  @Mapping(source = "debateRoomStatus", target = "debateStatus")
  DebateRoomGetDTO convertEntityToDebateRoomGetDTO(DebateRoom debateRoom);

  @Mapping(source = "roomId", target = "roomId")
  DebateRoom convertDebateRoomPutDTOtoEntity(DebateRoomPutDTO debateRoomPutDTO);

  @Mapping(source = "userId", target = "id")
  @Mapping(source = "username", target = "username")
  User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

}
