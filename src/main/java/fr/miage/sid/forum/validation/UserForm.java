package fr.miage.sid.forum.validation;

import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Component
public class UserForm {

  @NotEmpty
  @Email
  private String email;

  @Size(min = 3, max = 20)
  @NotEmpty
  private String username;

  @Size(min = 4, max = 100)
  @NotEmpty
  private String password;

  private String firstname;

  private String lastname;
}
