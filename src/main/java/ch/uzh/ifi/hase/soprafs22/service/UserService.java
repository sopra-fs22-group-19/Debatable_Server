package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Role;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService implements UserDetailsService{

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  @Autowired
  public UserService(
          @Qualifier("userRepository") UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }



  public User createUser(User newUser) {

    newUser.setToken(UUID.randomUUID().toString());
    newUser.setCreationDate(LocalDate.now());
    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
    newUser.setRole(Role.REGISTER);


    checkIfUsernameExists(newUser);


    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }



  public User createGuestUser() {
      User newUser = new User();

      String guestUsername = "GuestUser";

      if(userRepository.findByUsername(guestUsername) == null){
          newUser.setUsername(guestUsername);
          newUser.setName("Guest");
          newUser.setPassword(passwordEncoder.encode("password"));
          newUser.setToken(UUID.randomUUID().toString());
          newUser.setCreationDate(LocalDate.now());
          newUser.setRole(Role.GUEST);
          newUser = userRepository.save(newUser);
          userRepository.flush();
      }else {
          return userRepository.findByUsername(guestUsername);
      }

      log.debug("Created Information for GuestUser: {}", newUser);
      return newUser;
  }

  public User checkCredentials(String username, String password){

      User checkedUser = userRepository.findByUsername(username);

      if(checkedUser == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The provided username is invalid.");
      }

      if(checkedUser.getPassword().equals(password)){
          return checkedUser;
      }
      else{
          throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The password provided is incorrect");
      }
  }

  public void deleteUser(Long id) {
      this.userRepository.deleteById(id);
  }

  public User getUserByUserId(Long userId, String errorMessage){

      errorMessage = String.format("Error: reason <%s>", errorMessage);
      User user = userRepository.findByid(userId);

      if(user == null)
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);

      return user;
  }

  public User updateUser(Long id, User userDetails){

      String errorMessage = String.format("User with id: '%d' was not found", id);
      String errorMessagePassword = "The password must be different from the previous one.";
      String errorMessageUsername = "The selected username is already taken by another user, choose another username.";

      User toUpdateUser = userRepository.findByid(id);

      if(Objects.isNull(toUpdateUser)){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
      }

      if(Objects.equals(userDetails.getPassword(), toUpdateUser.getPassword())){
          throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessagePassword);
      }

      else{
          if(!Objects.isNull(userDetails.getName())){
              if(!userDetails.getName().isEmpty()){
                  toUpdateUser.setName(userDetails.getName());
              }
          }
          if(!Objects.isNull(userDetails.getUsername())){
              if(!userDetails.getUsername().isEmpty() && !Objects.equals(userDetails.getUsername(), toUpdateUser.getUsername())){
                  checkIfUsernameExists(userDetails);
                  toUpdateUser.setUsername(userDetails.getUsername());
              }
          }
          if(!Objects.isNull(userDetails.getPassword())){
              if(!userDetails.getPassword().isEmpty()){
                  toUpdateUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
              }
          }
          userRepository.saveAndFlush(toUpdateUser);

          return toUpdateUser;
      }
  }




  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */


  // changed to only check if username is unique, template check both username and name
  public void checkIfUsernameExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "The username provided is not unique. Therefore, the user could not be created!");

    }
  }



  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findByUsername(username);
      if(username == null){
          throw new UsernameNotFoundException("user not found.");
      }

      Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

      return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorities);
  }




}
