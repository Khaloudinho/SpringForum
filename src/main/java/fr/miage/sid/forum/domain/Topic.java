package fr.miage.sid.forum.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Topic extends HasPermissions implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  private String title;

  @ManyToOne
  private Project project;

  @ManyToMany
  private Set<User> followers = new HashSet<>();

  public void addFollower(User user) {
    this.followers.add(user);
  }

  public void removeFollower(User user) {
    this.followers.remove(user);
  }
}
