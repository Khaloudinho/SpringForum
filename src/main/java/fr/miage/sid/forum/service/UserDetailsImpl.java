package fr.miage.sid.forum.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetailsImpl extends User {

  private String firstname;
  private String lastname;
  private String email;

  public UserDetailsImpl(fr.miage.sid.forum.domain.User user,
      Collection<? extends GrantedAuthority> authorities) {
    super(user.getUsername(), user.getPassword(), authorities);
    this.firstname = user.getFirstname();
    this.lastname = user.getLastname();
    this.email = user.getEmail();
  }


  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }


}
