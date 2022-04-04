package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.time.LocalDate;

public class UserGetDTO {

  private Long userId;
  private String username;
  private String name;
  private LocalDate creation_date;
  private String token;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
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

  public LocalDate getCreation_date() {
    return creation_date;
  }

  public void setCreation_date(LocalDate creation_date) {
    this.creation_date = creation_date;
  }

  public String getToken() {
      return token;
  }

  public void setToken(String token) {
      this.token = token;
  }
}
