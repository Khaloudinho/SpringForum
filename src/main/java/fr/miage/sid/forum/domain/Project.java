package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.exception.PermissionTopicException;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Project extends HasPermissions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotEmpty
  private String name;

  @ManyToOne
  private User creator;

//    @OneToMany(mappedBy = "project")
//    private Set<Topic> topics= new HashSet<>();


  /**
   * addTopic, set a topic to the project
   *
   * @param topic Topic, topic set
   * @throws PermissionTopicException if the user can't insert the topic
   */
  public void addTopic(Topic topic) throws PermissionTopicException {
    if (this.hasPermission(topic.getCreator(), Permission.WRITE)) {
      topic.setProject(this);
//            this.topics.add(topic);
    } else {
      throw new PermissionTopicException("User has insufficient permissions to create this topic");
    }
  }

}
