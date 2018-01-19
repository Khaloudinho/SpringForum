package fr.miage.sid.forum.config.security;

import com.google.common.collect.Sets;
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
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Sso
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private UserRepository userRepository;

  private RoleRepository roleRepository;

  private UserDetailsService userDetailsService;

  @Autowired
  public WebSecurityConfig(UserRepository userRepository,
      RoleRepository roleRepository,
      UserDetailsService userDetailsService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public PermissionService permissionService() {
    return new PermissionService();
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
      User user = userRepository.eagerFindByOauthId(oauthId);
      if (user == null) {
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
        // We will update picture every time to make sure our data is fresh
        user.setPicture((String) map.get("picture"));
      }

      User saved = userRepository.save(user);
      saved.setPassword(""); // To prevent error with UserDetails super() call
      return new UserDetailsImpl(saved, getAuthorities(saved.getRoles()));
    };
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    super.configure(auth);
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
        .ignoring()
        .antMatchers("/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/**/*.js");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .formLogin().loginPage("/login").usernameParameter("email").defaultSuccessUrl("/")
        .and().logout().logoutSuccessUrl("/").deleteCookies("JSESSIONID", "SESSION")
        .and().exceptionHandling().accessDeniedPage("/403")
        // Needed to redirect to login when not authenticated
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        .and().authorizeRequests()
        .antMatchers("/").permitAll()
        .and().rememberMe();
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
    List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getRole()));
    }
    return authorities;
  }
}
