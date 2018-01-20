package fr.miage.sid.forum.service;

import com.google.common.collect.Sets;
import fr.miage.sid.forum.config.security.UserDetailsImpl;
import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.RoleRepository;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import fr.miage.sid.forum.domain.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
      @Lazy BCryptPasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User save(User user) {
    user.setOrigin(UserOrigin.DB);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Role userRole = roleRepo.findByRole("ROLE_USER");
    Set<Role> roles = Sets.newHashSet(userRole);
    user.setRoles(roles);
    return userRepo.save(user);
  }

  @Override
  public User getOne(Long id) {
    return userRepo.getOne(id);
  }

  @Override
  public List<User> getAll() {
    return userRepo.findAll();
  }

  @Override
  public UserDetailsImpl getUserDetails(User user) {
    return new UserDetailsImpl(user, getAuthorities(user.getRoles()));
  }

  @Override
  public User eagerFindByEmail(String email) {
    return userRepo.eagerFindByEmail(email);
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
    List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getRole()));
    }
    return authorities;
  }
}
