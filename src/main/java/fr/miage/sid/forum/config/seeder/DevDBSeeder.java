package fr.miage.sid.forum.config.seeder;

import com.google.common.collect.Sets;
import fr.miage.sid.forum.domain.Permission;
import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import fr.miage.sid.forum.domain.ProjectRepository;
import fr.miage.sid.forum.domain.RoleRepository;
import fr.miage.sid.forum.domain.TopicRepository;
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

    User dummy = new User();
    dummy.setFirstname("System").setLastname("System").setUsername("system")
        .setEmail("system@spring.com").setPassword(passwordEncoder.encode("system"))
        .setRoles(Sets.newHashSet(userRole)).setOrigin(UserOrigin.DB);
    dummy = userRepo.save(dummy);

    Project projectA = new Project().setName("Projet A");
    Project projectB = new Project().setName("Projet B");
    projectRepository.save(projectA);
    projectRepository.save(projectB);

    Topic topicA1 = new Topic().setTitle("Premier sujet").setProject(projectA);
    Topic topicB1 = new Topic().setTitle("Vous n'allez jamais croire ce qu'il s'est passé ! :xxx")
        .setProject(projectB);
    topicA1.givePermission(dummy.getId(), Permission.ALL);
    topicB1.givePermission(dummy.getId(), Permission.ALL);
    topicRepository.save(topicA1);
  }
}
