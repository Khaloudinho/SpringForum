package fr.miage.sid.forum.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.miage.sid.forum.repository.UserRepository;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataAuditTests {

  @Autowired
  private UserRepository userRepo;

  private User user;

  @Before
  public void create() {
    User newUser = new User().setUsername("test").setPassword("test")
        .setEmail("test@test.com");
    user = userRepo.save(newUser);
  }

  @Test
  public void testUpdatedAt() {
    Date updated = user.getUpdatedAt();

    User updateUser = new User();
    updateUser.setId(user.getId());
    updateUser.setUsername("changed");
    updateUser.setEmail(user.getEmail());
    updateUser.setPassword(user.getPassword());

    userRepo.save(updateUser);

    User updatedUser = userRepo.findOne(user.getId());
    assertThat(updatedUser.getUsername()).isEqualToIgnoringCase("changed");
    assertThat(updatedUser.getUpdatedAt()).isAfter(updated);
  }


}
