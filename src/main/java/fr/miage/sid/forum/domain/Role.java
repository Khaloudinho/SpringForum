package fr.miage.sid.forum.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
* A User can have many roles, such as ROLE_ADMIN or ROLE_USER
*/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
public class Role implements Serializable {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String role;
}
