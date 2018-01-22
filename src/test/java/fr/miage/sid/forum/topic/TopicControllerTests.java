package fr.miage.sid.forum.topic;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import fr.miage.sid.forum.config.security.MethodSecurityConfig;
import fr.miage.sid.forum.config.security.PermissionService;
import fr.miage.sid.forum.config.security.WebSecurityConfig;
import fr.miage.sid.forum.config.security.WithMockMyPrincipal;
import fr.miage.sid.forum.controller.TopicController;
import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(TopicController.class)
@Import({WebSecurityConfig.class, MethodSecurityConfig.class})
public class TopicControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @MockBean
  private TopicService topicService;

  @MockBean
  private UserService userService;

  @MockBean
  private PermissionService permissionService;

  @MockBean
  private ResourceServerProperties resourceServerProperties;

  @Before
  public void setup() {
    User creator = new User().setUsername("creator").setId(2L);
    Project project = new Project().setId(2L).setName("Spring Data");
    project.setCreatedBy(creator);
    Topic topic = new Topic().setId(2L).setTitle("How To").setProject(project);
    topic.setCreatedBy(creator);
    when(permissionService.canReadTopic(anyLong())).thenReturn(true);
    when(topicService.getOne(anyLong())).thenReturn(topic);
  }


  @WithMockMyPrincipal
  @Test
  public void canReadTopicIfHasPermission() throws Exception {
    mockMvc.perform(get("/topic/{topicId}", 1L))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("topic/show"))
        .andExpect(model().attributeExists("topic"))
        .andExpect(model().attribute("topic", hasProperty("title", is("How To"))))
        .andExpect(model().attributeExists("posts"));
  }

}
