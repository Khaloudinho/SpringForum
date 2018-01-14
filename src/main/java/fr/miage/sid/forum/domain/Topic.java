package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.exception.PermissionPostException;
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
public class Topic extends HasPermissions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  private String title;

  @ManyToOne
  private User creator;

  @ManyToOne
  private Project project;

//    @OneToMany(mappedBy = "topic")
//    private Set<Post> posts = new HashSet<>();

  // TODO We need a way to check if everyobdy is allowed to post, etc (checking empty on writers) ?
  public void addPost(Post post) throws PermissionPostException {
    if (this.hasPermission(post.getUser(), Permission.WRITE)) {
//      this.posts.add(post);
      post.setTopic(this);
    } else {
      throw new PermissionPostException("User has insufficient permissions to create this post");
    }
  }

}
