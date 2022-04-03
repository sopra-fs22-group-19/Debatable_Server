package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class UserPostDTO {

  private String password;

  private String username;

  private String name;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
}
