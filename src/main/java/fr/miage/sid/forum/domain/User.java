package fr.miage.sid.forum.domain;


import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@NoArgsConstructor
@Entity
// We specify table name because we can't have an User table in psql
@Table(name = "Users")
@NamedEntityGraph(name = "User.detail", attributeNodes = @NamedAttributeNode("roles"))
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @NotEmpty
  @Email
  private String email;

  @Size(min = 3, max = 20)
  @NotEmpty
  private String username;

  @Size(min = 6, max = 100)
  @NotEmpty
  private String password;

  private String firstname;

  private String lastname;

  @ManyToMany
  private Set<Role> roles;

  private boolean enabled = true;
}
