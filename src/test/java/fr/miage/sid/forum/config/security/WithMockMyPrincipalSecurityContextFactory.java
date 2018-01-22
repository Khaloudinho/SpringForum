package fr.miage.sid.forum.config.security;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockMyPrincipalSecurityContextFactory implements
    WithSecurityContextFactory<WithMockMyPrincipal> {

  @Override
  public SecurityContext createSecurityContext(WithMockMyPrincipal withMockMyPrincipal) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    User user = new User().setId(Long.valueOf(withMockMyPrincipal.id()))
        .setUsername(withMockMyPrincipal.username()).setPassword(withMockMyPrincipal.password())
        .setOrigin(UserOrigin.DB);

    for (String role : withMockMyPrincipal.roles()) {
      grantedAuthorities.add(new SimpleGrantedAuthority(role));
    }

    UserDetails principal = new UserDetailsImpl(user, grantedAuthorities);
    Authentication authentication = new UsernamePasswordAuthenticationToken(principal,
        withMockMyPrincipal.password(), grantedAuthorities);
    context.setAuthentication(authentication);
    return context;
  }
}
