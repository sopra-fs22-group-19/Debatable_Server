package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);

    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @GetMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO checkUserCredentials(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "password") String password){

      User verifiedUser = userService.checkCredentials(username, password);

      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(verifiedUser);
  }


  //create temp guest user
  @PostMapping("/users/guests")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createGuestUser() {

      User guestUser = new User();

      User createdUser = userService.createGuestUser(guestUser);

      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  //to delete guest user
  @DeleteMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteUser(@PathVariable Long id) {
      userService.deleteUser(id);
  }

  @GetMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUserDBbyID(@PathVariable("id") Long id) {

      String errorMessage = String.format("User with id: '%d' was not found", id);
      User user = userService.getUserByUserId(id, errorMessage);

      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
  }

  @PutMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO updateUserDBDetails(@PathVariable("id") Long id, @RequestBody UserPutDTO userPutDTO){

      User userDetails = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

      User updatedUser = userService.updateUser(id, userDetails);

      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
  }


}
