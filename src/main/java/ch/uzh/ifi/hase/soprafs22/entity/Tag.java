package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serializable;

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
@Table(name = "TAG")
public class Tag implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long tagID;

  @Column(nullable = false)
  private String name;

  @Column
  private Boolean isFilterTag;

  public Long getTagID() {
    return tagID;
  }

  public void setTagID(Long tagID) {
    this.tagID = tagID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getIsFilterTag() {
    return isFilterTag;
  }

  public void setIsFilterTag(Boolean isFilterTag) {
    this.isFilterTag = isFilterTag;
  }

}
