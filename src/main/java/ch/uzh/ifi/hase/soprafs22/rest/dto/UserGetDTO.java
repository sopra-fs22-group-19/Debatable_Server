package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.time.LocalDate;

public class UserGetDTO {

  private Long id;
  private String username;
<<<<<<< HEAD
  private UserStatus status;
  private String password;
=======
  private String name;
  private LocalDate creationDate;
  private String token;
>>>>>>> origin/dev

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
        return name;
    }

  public void setName(String name) {
        this.name = name;
    }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public String getToken() {
      return token;
  }

  public void setToken(String token) {
      this.token = token;
  }

  public String getPassword() {
        return password;
    }

  public void setPassword(String password) {
        this.password = password;
    }
}
