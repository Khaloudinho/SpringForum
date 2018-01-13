package fr.miage.sid.forum.seeder;

import com.google.common.collect.Sets;
import fr.miage.sid.forum.domain.EDroit;
import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import fr.miage.sid.forum.repository.ProjectRepository;
import fr.miage.sid.forum.repository.RoleRepository;
import fr.miage.sid.forum.repository.TopicRepository;
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
  private ProjectRepository projectRepository;
  private TopicRepository topicRepository;
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public DBSeeder(RoleRepository roleRepo, UserRepository userRepo,
      ProjectRepository projectRepository,
      TopicRepository topicRepository,
      BCryptPasswordEncoder passwordEncoder) {
    this.roleRepo = roleRepo;
    this.userRepo = userRepo;
    this.projectRepository = projectRepository;
    this.topicRepository = topicRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @EventListener
  @Transactional
  public void seed(ContextRefreshedEvent ev) {
    log.info("Seeding database with roles and a dummy user");
    Role userRole = createRoleIfNotExists("ROLE_USER");

    User dummy = new User();
    dummy.setFirstname("John").setLastname("Doe").setUsername("johndoe")
        .setEmail("john@doe.com").setPassword(passwordEncoder.encode("test"))
        .setRoles(Sets.newHashSet(userRole)).setOrigin(UserOrigin.DB);
    userRepo.save(dummy);

    Project projectA = new Project().setName("Projet A").setCreator(dummy);
    Project projectB = new Project().setName("Projet B").setCreator(dummy);
    projectRepository.save(projectA);
    projectRepository.save(projectB);

    Topic topicA1 = new Topic().setTitle("Premier sujet").setCreator(dummy).setProject(projectA);
    topicA1.addDroit(dummy, EDroit.ALL);
    Topic topicB1 = new Topic().setTitle("Vous n'allez jamais croire ce qu'il s'est passé ! :xxx")
        .setCreator(dummy).setProject(projectB);
    topicB1.addDroit(dummy, EDroit.ALL);
    topicRepository.save(topicA1);
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
