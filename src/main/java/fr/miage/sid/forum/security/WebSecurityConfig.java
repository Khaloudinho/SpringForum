package fr.miage.sid.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private UserDetailsService userDetailsService;

  private AccessDeniedHandler accessDeniedHandler;

  @Autowired
  public WebSecurityConfig(UserDetailsService userDetailsService,
      AccessDeniedHandler accessDeniedHandler) {
    this.userDetailsService = userDetailsService;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
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
