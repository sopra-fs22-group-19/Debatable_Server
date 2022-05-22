package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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

  private final UserRepository userRepository;

  UserController(UserService userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }

  @PostMapping("/register")
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

  /*
  @GetMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO checkUserCredentials(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "password") String password){

      User verifiedUser = userService.checkCredentials(username, password);

      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(verifiedUser);
  }

   */

  @GetMapping("/login/v2")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO checkUserAuth(Authentication authentication){
      String username = authentication.getName();

      User verifiedUser = userRepository.findByUsername(username);

      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(verifiedUser);
  }


  //create temp guest user
  @PostMapping("/register/guests")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createGuestUser() {

      User createdUser = userService.createGuestUser();

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
