package fr.miage.sid.forum.config.seeder;

import com.google.common.collect.Sets;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import fr.miage.sid.forum.domain.RoleRepository;
import fr.miage.sid.forum.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class TestDBSeeder implements CommandLineRunner {

  private RoleRepository roleRepo;
  private UserRepository userRepo;
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public TestDBSeeder(RoleRepository roleRepo,
      UserRepository userRepo,
      BCryptPasswordEncoder passwordEncoder) {
    this.roleRepo = roleRepo;
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... strings) throws Exception {
    log.info("Seeding test database");

    Role userRole = roleRepo.save(new Role().setRole("ROLE_USER"));

    User dummy = new User();
    dummy.setFirstname("Test").setLastname("Test").setUsername("user")
        .setEmail("user@test.com").setPassword(passwordEncoder.encode("password"))
        .setRoles(Sets.newHashSet(userRole)).setOrigin(UserOrigin.DB);
    userRepo.save(dummy);
  }
}
