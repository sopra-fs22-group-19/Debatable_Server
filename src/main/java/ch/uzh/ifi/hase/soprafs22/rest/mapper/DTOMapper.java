package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.DebateRoom;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.DebateRoomPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
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

  // DTOs for the user
  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  UserGetDTO convertEntityToUserGetDTO(User user);

  // DTOs for the DebateRoom
  @Mapping(source = "userId", target = "creatorUserId")
  @Mapping(source = "debateId", target = "debateTopicId")
  DebateRoom convertDebateRoomPostDTOtoEntity(DebateRoomPostDTO debateRoomPostDTO);

  @Mapping(source = "roomId", target = "roomId")
  @Mapping(source = "debateRoomStatus", target = "debateStatus")
  DebateRoomGetDTO convertEntityToDebateGetDTO(DebateRoom DebateRoom);

}
