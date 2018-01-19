package fr.miage.sid.forum.config.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetailsImpl extends User implements MyPrincipal {

  private Long id;
  private String name;
  private String email;

  public UserDetailsImpl(fr.miage.sid.forum.domain.User user,
      Collection<? extends GrantedAuthority> authorities) {
    super(user.getUsername(), user.getPassword(), authorities);
    this.name = user.getUsername();
    this.email = user.getEmail();
    this.id = user.getId();
  }


  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public boolean isAdmin() {
    return getAuthorities().stream()
        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }
}
