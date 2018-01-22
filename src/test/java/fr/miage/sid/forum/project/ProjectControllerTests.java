package fr.miage.sid.forum.project;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.miage.sid.forum.config.security.MethodSecurityConfig;
import fr.miage.sid.forum.config.security.PermissionService;
import fr.miage.sid.forum.config.security.WebSecurityConfig;
import fr.miage.sid.forum.controller.ProjectController;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
@Import({WebSecurityConfig.class, MethodSecurityConfig.class})
public class ProjectControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProjectService projectService;

  @MockBean
  private TopicService topicService;

  @MockBean
  private UserService userService;

  @MockBean
  private PermissionService permissionService;

  @MockBean
  private ResourceServerProperties resourceServerProperties;

  @Test
  @WithAnonymousUser
  public void canAlwaysAccessProjectsIndex() throws Exception {
    mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk());
  }

}
