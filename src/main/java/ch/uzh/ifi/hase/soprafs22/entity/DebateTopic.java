package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.TopicCategory;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import javax.persistence.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

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
@Table(name = "DEBATETOPICS")
public class DebateTopic implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(
          name = "topic_seq",
          sequenceName = "topic_seq",
          allocationSize = 1
  )
  //avoid using same generator with userid
  //e.g.(user1 id=1 ,user2 id=2, topic1 id=3, topic2 id=4)
  @GeneratedValue(
          strategy = SEQUENCE,
          generator = "topic_seq"
  )
  private Long debateTopicId;

  @ManyToOne
  @JoinColumn(name = "creator_user_id", nullable = false)
  private User creatorUser;

  @Column(nullable = false)
  private String topic;

  @Column
  private String topicDescription;

  @Column(nullable = false)
  private boolean isDefaultTopic = false;

  @Column
  private TopicCategory category;

  @OneToMany(mappedBy="debateTopic")
  private List<DebateRoom> debateRoomSet;

  public Long getDebateTopicId() {
    return debateTopicId;
  }

  public void setDebateTopicId(Long debateTopicId) {
    this.debateTopicId = debateTopicId;
  }

  public User getCreatorUser() {
        return creatorUser;
    }

  public void setCreatorUser(User creatorUser) {
        this.creatorUser = creatorUser;
    }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getTopicDescription() {
        return topicDescription;
    }

  public void setTopicDescription(String topicDescription) {
    this.topicDescription = topicDescription;
  }

  public boolean getIsDefaultTopic() {
        return isDefaultTopic;
    }


  public void setIsDefaultTopic(boolean isDefaultTopic) {
        this.isDefaultTopic = isDefaultTopic;
    }


  public TopicCategory getCategory(){return category;}

  public void setCategory(TopicCategory category){this.category = category;}



  public static List<DebateTopic> readTopicListCSV(String filepath) throws IOException, CsvValidationException {

  public Long getCreatorUserId(){
      if (this.creatorUser != null)
          return this.creatorUser.getId();
      else
          return null;
  }
  public static List<DebateTopic> readTopicListCSV(String filepath, User defaultUser) throws IOException, CsvValidationException {


      CSVReader csvReader = new CSVReaderBuilder(new FileReader(filepath)).withSkipLines(1).build();
      ArrayList<DebateTopic> debateTopics=  new ArrayList<>();

      String[] line;
      while ((line = csvReader.readNext()) != null) {
          DebateTopic debateTopic = new DebateTopic();
          debateTopic.setTopic(line[0]);
          debateTopic.setTopicDescription(line[1]);
          debateTopic.setCreatorUser(defaultUser);
          debateTopic.setIsDefaultTopic(true);

          debateTopics.add(debateTopic);

      }

      return debateTopics;

  }


}
