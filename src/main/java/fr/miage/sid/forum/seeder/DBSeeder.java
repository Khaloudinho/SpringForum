package fr.miage.sid.forum.seeder;

import com.google.common.collect.Sets;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import fr.miage.sid.forum.repository.RoleRepository;
import fr.miage.sid.forum.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class DBSeeder {

  private RoleRepository roleRepo;
  private UserRepository userRepo;
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public DBSeeder(RoleRepository roleRepo, UserRepository userRepo,
      BCryptPasswordEncoder passwordEncoder) {
    this.roleRepo = roleRepo;
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @EventListener
  @Transactional
  public void seed(ContextRefreshedEvent ev) {
    log.info("Seeding database with roles and a dummy user");
    Role userRole = createRoleIfNotExists("ROLE_USER");

    User dummy = new User().setFirstname("John").setLastname("Doe").setUsername("johndoe")
        .setEmail("john@doe.com").setPassword(passwordEncoder.encode("test"))
        .setRoles(Sets.newHashSet(userRole)).setOrigin(UserOrigin.DB);
    userRepo.save(dummy);
  }


  private Role createRoleIfNotExists(String name) {
    Role role = roleRepo.findByRole(name);

    if (role == null) {
      role = new Role().setRole(name);
      roleRepo.save(role);
    }

    return role;
  }
}
