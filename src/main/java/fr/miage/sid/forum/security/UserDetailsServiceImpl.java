package fr.miage.sid.forum.security;

import fr.miage.sid.forum.domain.Role;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private UserRepository userRepo;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.info("Loading user by email: " + email);
    User user = userRepo.eagerFindByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException(email);
    }

    return new UserDetailsImpl(user, getAuthorities(user.getRoles()));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
    List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getRole()));
    }
    return authorities;
  }
}
