package fr.miage.sid.forum.config;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
@Configuration
public class OAuthConfig {

  private UserRepository userRepository;

  private RoleRepository roleRepository;

  @Autowired
  public OAuthConfig(UserRepository userRepository,
      RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  /**
   * This Bean is used to extract OAuth2 connection information
   * and then save them to DB. By using this extractor we make sure that
   * we can always map an User (coming from OAuth or from classic registration) to
   * a know MyPrincipal interface. This allows us to use the @CurrentUser
   * annotation in our controllers to always retrieve a Principal with a known implementation
   * (and also decoupling from Spring Security).
   */
  @Bean
  public PrincipalExtractor principalExtractor() {
    return map -> {
      String oauthId = (String) map.get("sub");
      User user = userRepository.eagerFindByOauthId(oauthId);
      if (user == null) {
        log.info("Mapping google user and saving it to DB");
        user = new User();
        Role userRole = roleRepository.findByRole("ROLE_USER");
        user.setEmail((String) map.get("email"))
            .setOauthId(oauthId).setUsername((String) map.get("name"))
            .setFirstname((String) map.get("given_name"))
            .setLastname((String) map.get("family_name"))
            .setPicture((String) map.get("picture"))
            .setRoles(Sets.newHashSet(userRole))
            .setOrigin(UserOrigin.GOOGLE);
      } else {
        log.info("Google auth existing user found, updating profile pic");
        // We will update picture every time to make sure our data is fresh
        user.setPicture((String) map.get("picture"));
      }

      User saved = userRepository.save(user);
      saved.setPassword(""); // To prevent error with UserDetails super() call
      return new UserDetailsImpl(saved, getAuthorities(saved.getRoles()));
    };
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
    List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getRole()));
    }
    return authorities;
  }
}
