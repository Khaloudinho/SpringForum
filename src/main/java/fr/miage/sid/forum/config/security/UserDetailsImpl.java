package fr.miage.sid.forum.config.security;

import fr.miage.sid.forum.domain.UserOrigin;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetailsImpl extends User implements MyPrincipal {

  private Long id;
  private String name;
  private String email;
  private UserOrigin origin;

  public UserDetailsImpl(fr.miage.sid.forum.domain.User user,
      Collection<? extends GrantedAuthority> authorities) {
    super(user.getUsername(), user.getPassword(), authorities);
    this.name = user.getUsername();
    this.email = user.getEmail();
    this.id = user.getId();
    this.origin = user.getOrigin();
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
  public boolean isFromDB() {
    return origin.equals(UserOrigin.DB);
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
