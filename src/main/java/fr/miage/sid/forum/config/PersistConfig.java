package fr.miage.sid.forum.config;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.security.MyPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class PersistConfig {

  @Bean
  public AuditorAware<User> auditorProvider() {
    return () -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
        MyPrincipal principal = (MyPrincipal) authentication.getPrincipal();
        return new User().setId(principal.getId());
      }

      // Will be system account created in Seeder
      return new User().setId(1L);
    };
  }

}
