package fr.miage.sid.forum.config.security;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.info("Loading user by email: " + email);
    User user = userService.eagerFindByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException(email);
    }

    return userService.getUserDetails(user);
  }
}
