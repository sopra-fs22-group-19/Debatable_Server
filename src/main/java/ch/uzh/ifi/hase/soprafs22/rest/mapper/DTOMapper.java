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


  @Mapping(source = "password", target = "password")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "token", target = "token")
  UserGetDTO convertEntityToUserGetDTO(User user);

  // DTOs for the DebateTopic
  @Mapping(source = "debateTopicId", target = "userId")
  @Mapping(source = "creatorUserId", target = "debateId")
  @Mapping(source = "topic", target = "topic")
  @Mapping(source = "topicDescription", target = "description")
  DebateTopicGetDTO convertEntityToDebateDTO(DebateTopic debateTopic);
  
  // DTOs for the DebateRoom
  @Mapping(source = "userId", target = "creatorUserId")
  @Mapping(source = "debateId", target = "debateTopicId")
  DebateRoom convertDebateRoomPostDTOtoEntity(DebateRoomPostDTO debateRoomPostDTO);

  @Mapping(source = "roomId", target = "roomId")
  @Mapping(source = "debateRoomStatus", target = "debateStatus")
  @Mapping(source = "debateTopic", target = "debate")
  DebateRoomGetDTO convertEntityToDebateRoomGetDTO(DebateRoom DebateRoom);

}
