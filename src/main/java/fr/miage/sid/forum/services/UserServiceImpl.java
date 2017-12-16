package fr.miage.sid.forum.services;

import com.google.common.collect.Sets;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.repository.RoleRepository;
import fr.miage.sid.forum.repository.UserRepository;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepo;
  private RoleRepository roleRepo;
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepo,
      RoleRepository roleRepo,
      BCryptPasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepo.findByEmail(email);
  }

  @Override
  public void save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    // TODO Need to make sure user role is defined at startup
    Role userRole = roleRepo.findByRole("USER");
    Set<Role> roles = Sets.newHashSet(userRole);
    user.setRoles(roles);
  }
}
