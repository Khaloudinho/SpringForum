package fr.miage.sid.forum.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Entity
@Table(name = "Person")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty
  @Email
  private String email;
  @Size(min = 3, max = 10)
  private String username;
  @Size(min = 6, max = 100)
  private String password;
  @NotEmpty
  private String firstname;
  @NotEmpty
  private String lastname;
}
