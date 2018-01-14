package fr.miage.sid.forum.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class SpringSecurityIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testLoginSuccess() throws Exception {
    RequestBuilder builder = formLogin().userParameter("email").user("user@test.com")
        .password("password");
    mockMvc.perform(builder).andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andExpect(authenticated().withUsername("user"));
  }

  @Test
  public void testInvalidPassword() throws Exception {
    RequestBuilder builder = formLogin().userParameter("email").user("user@test.com")
        .password("invalid");
    mockMvc.perform(builder).andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?error"))
        .andExpect(unauthenticated());
  }
}
