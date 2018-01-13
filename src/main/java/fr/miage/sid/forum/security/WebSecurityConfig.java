package fr.miage.sid.forum.security;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserOrigin;
import fr.miage.sid.forum.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Sso
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private UserRepository userRepository;

  private UserDetailsService userDetailsService;

  private AccessDeniedHandler accessDeniedHandler;

  @Autowired
  public WebSecurityConfig(UserRepository userRepository,
      UserDetailsService userDetailsService,
      AccessDeniedHandler accessDeniedHandler) {
    this.userRepository = userRepository;
    this.userDetailsService = userDetailsService;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
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
      log.info("Mapping google user and saving it to DB");
      String oauthId = (String) map.get("sub");
      User user = userRepository.findByOauthId(oauthId);
      if (user == null) {
        user = new User();
        user.setEmail((String) map.get("email"))
            .setOauthId(oauthId).setUsername((String) map.get("name"))
            .setFirstname((String) map.get("given_name"))
            .setLastname((String) map.get("family_name"))
            .setPicture((String) map.get("picture"))
            .setOrigin(UserOrigin.GOOGLE);
      } else {
        // We will update picture every time to make sure our data is fresh
        user.setPicture((String) map.get("picture"));
      }

      userRepository.save(user);
      return user;
    };
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/").permitAll()
//          .antMatchers("/admin/**").hasAnyRole("ADMIN") //all admins patterns
//          .antMatchers("/user/**").hasAnyRole("USER") //all users patterns
//          .anyRequest().authenticated()
        .and().formLogin().loginPage("/login").usernameParameter("email").permitAll()
        .and().logout().logoutSuccessUrl("/").permitAll()
        .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
  }
}
