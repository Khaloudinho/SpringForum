package fr.miage.sid.forum.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private DataSource dataSource; // Don't worry about the Intellij warning ..

  @Autowired
  private AccessDeniedHandler accessDeniedHandler;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // TODO We still need to check the thing with role prefixes
    auth.jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery("select email, password, enabled from users where email=?")
        .authoritiesByUsernameQuery("SELECT\n"
            + "  u.email,\n"
            + "  r.role\n"
            + "FROM users u \n"
            + "INNER JOIN users_roles ur ON u.id = ur.user_id\n"
            + "INNER JOIN role r ON ur.roles_id = r.id where u.email=?")
        .passwordEncoder(bCryptPasswordEncoder);
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
