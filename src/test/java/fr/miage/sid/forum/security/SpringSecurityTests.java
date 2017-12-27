package fr.miage.sid.forum.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserService userService;

  @Before
  public void create() {
    User user = new User().setUsername("test")
        .setPassword("test")
        .setEmail("test@test.com");
    userService.save(user);
  }


  @Test
  public void testLoginSuccess() throws Exception {
    RequestBuilder builder = formLogin().userParameter("email").user("test@test.com")
        .password("test");
    mockMvc.perform(builder).andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andExpect(authenticated().withUsername("test"));


  }

  @Test
  public void testLoginFailed() throws Exception {
    RequestBuilder builder = formLogin().userParameter("email").user("test@test.com")
        .password("invalid");
    mockMvc.perform(builder).andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?error"))
        .andExpect(unauthenticated());
  }

}
