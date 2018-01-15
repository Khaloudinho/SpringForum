package fr.miage.sid.forum.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

//  /**
//   * You can add a Topic if writers is empty (everybody can post) or if you are in writers
//   *
//   * @throws PermissionTopicException Not allowed to create topic
//   */
//  public void addTopic(Topic topic) throws PermissionTopicException {
//    if (this.hasPermission(topic.getCreatedBy().getId(), Permission.WRITE)) {
//      topic.setProject(this);
////            this.topics.add(topic);
//    } else {
//      throw new PermissionTopicException("User has insufficient permissions to create this topic");
//    }
//  }

}
