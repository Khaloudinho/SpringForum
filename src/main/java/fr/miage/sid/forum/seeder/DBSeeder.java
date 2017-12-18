package fr.miage.sid.forum.seeder;

import com.google.common.collect.Sets;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.repository.RoleRepository;
import fr.miage.sid.forum.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DBSeeder {

  private Logger logger = LoggerFactory.getLogger(DBSeeder.class);
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
    logger.info("Seeding database with roles and a dummy user");
    Role userRole = createRoleIfNotExists("USER");

    User dummy = new User();
    dummy.setFirstname("John");
    dummy.setLastname("Doe");
    dummy.setUsername("johndoe");
    dummy.setEmail("john@doe.com");
    dummy.setPassword(passwordEncoder.encode("test"));
    dummy.setRoles(Sets.newHashSet(userRole));
    userRepo.save(dummy);
  }


  private Role createRoleIfNotExists(String name) {
    Role role = roleRepo.findByRole(name);

    if (role == null) {
      role = new Role();
      role.setRole(name);
      roleRepo.save(role);
    }

    return role;
  }
}
