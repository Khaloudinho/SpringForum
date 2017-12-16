package fr.miage.sid.forum.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private DataSource dataSource;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery("select username, password, enabled from person where username=?")
        .authoritiesByUsernameQuery(
            "SELECT\n"
                + "  username,\n"
                + "  role\n"
                + "FROM person\n"
                + "  INNER JOIN person_roles prole ON person.id = prole.user_id\n"
                + "  INNER JOIN role r ON prole.roles_id = r.id where username=?")
        .withUser("user").password("password").roles("USER");
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().permitAll()
        .and()
        .formLogin();
  }
}
