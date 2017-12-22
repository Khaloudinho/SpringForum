package fr.miage.sid.forum.domain;


import fr.miage.sid.forum.security.MyPrincipal;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
// We specify table name because we can't have an User table in psql
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable implements MyPrincipal, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String oauthId;

  private String email;

  private String username;

  private String password;

  private String firstname;

  private String lastname;

  private String picture;

  private UserOrigin origin;

  @ManyToMany
  private Set<Role> roles;

  private boolean enabled = true;

  @Override
  public String getName() {
    return firstname + " " + lastname;
  }

  // We need to have a small toString to prevent some problems with Google OAuth
  @Override
  public String toString() {
    return getName();
  }
}
