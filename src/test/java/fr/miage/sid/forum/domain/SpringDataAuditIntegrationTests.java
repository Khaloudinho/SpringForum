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
public class SpringDataAuditIntegrationTests {

  @Autowired
  private UserRepository userRepo;

  private User user;

  @Before
  public void create() {
    user = userRepo.findByUsername("user");
  }

  @Test
  public void testUpdatedAt() {
    Date updated = user.getUpdatedAt();

    User updateUser = new User().setId(user.getId()).setUsername("changed")
        .setEmail(user.getEmail())
        .setPassword(user.getPassword());

    userRepo.save(updateUser);

    User updatedUser = userRepo.findOne(user.getId());
    System.out.println(updatedUser.getEmail());
    System.out.println(updatedUser.getId());
    System.out.println(updatedUser.getUpdatedAt());
    assertThat(updatedUser.getUsername()).isEqualToIgnoringCase("changed");
    assertThat(updatedUser.getUpdatedAt()).isAfter(updated);
  }

}
