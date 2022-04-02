package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USERS")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  //remove user and status from template, add password and creationDate
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private LocalDate creationDate;

  @Column(nullable = false, unique = true)
  private String token;

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

  // newly added getter and setter for password and creation_date
  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
      this.password = password;
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

}
