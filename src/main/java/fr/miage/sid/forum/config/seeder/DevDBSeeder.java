package fr.miage.sid.forum.config.seeder;

import com.google.common.collect.Sets;
import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.ProjectRepository;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.RoleRepository;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.TopicRepository;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import fr.miage.sid.forum.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("dev")
@Slf4j
public class DevDBSeeder implements CommandLineRunner {

  private RoleRepository roleRepo;
  private UserRepository userRepo;
  private ProjectRepository projectRepository;
  private TopicRepository topicRepository;
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public DevDBSeeder(RoleRepository roleRepo, UserRepository userRepo,
      ProjectRepository projectRepository,
      TopicRepository topicRepository,
      BCryptPasswordEncoder passwordEncoder) {
    this.roleRepo = roleRepo;
    this.userRepo = userRepo;
    this.projectRepository = projectRepository;
    this.topicRepository = topicRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void run(String... strings) throws Exception {
    log.info("Seeding dev database");

    Role userRole = roleRepo.save(new Role().setRole("ROLE_USER"));
    Role adminRole = roleRepo.save(new Role().setRole("ROLE_ADMIN"));

    User dummy = new User();
    dummy.setFirstname("Admin").setLastname("Admin").setUsername("admin")
        .setEmail("admin@forum.com").setPassword(passwordEncoder.encode("admin"))
        .setRoles(Sets.newHashSet(userRole, adminRole)).setOrigin(UserOrigin.DB);
    userRepo.save(dummy);

    Project projectA = new Project().setName("Spring Data");
    Project projectB = new Project().setName("Spring Boot");
    projectRepository.save(projectA);
    projectRepository.save(projectB);

    Topic topicA1 = new Topic().setTitle("Comment avoir une repository REST?").setProject(projectA);
    Topic topicB1 = new Topic().setTitle("@SpringBoot c'est cool").setProject(projectB);
//    topicA1.givePermissionTo(dummy.getId(), Permission.ALL);
    topicRepository.save(topicA1);
    topicRepository.save(topicB1);
  }
}
