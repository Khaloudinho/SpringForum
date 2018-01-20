package fr.miage.sid.forum.post;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import fr.miage.sid.forum.config.security.MethodSecurityConfig;
import fr.miage.sid.forum.config.security.WebSecurityConfig;
import fr.miage.sid.forum.config.security.WithMockMyPrincipal;
import fr.miage.sid.forum.controller.PostController;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.exception.TopicNotFoundException;
import fr.miage.sid.forum.service.PostService;
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
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
@Import({WebSecurityConfig.class, MethodSecurityConfig.class})
public class PostControllerTests {

  @Autowired
  private MockMvc mockMvc;


  @MockBean
  private ResourceServerProperties resourceServerProperties;

  @MockBean
  private PostService postService;

  @MockBean
  private TopicService topicService;

  @MockBean
  private UserService userService;

  @MockBean
  private ProjectService projectService;

  @MockBean
  private JmsTemplate jmsTemplate;

  @Test
  @WithMockMyPrincipal
  public void cantAccessPostCreationIfNotAuth() throws Exception {
    mockMvc.perform(get("/topic/{topicId}/post/create", 1))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"));
  }

  @Test
  @WithAnonymousUser
  public void cantCreatePostIfNotAuth() throws Exception {
    mockMvc.perform(post("/topic/{topicId}/post", 1))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  public void cantUpdatePostIfNotAuth() throws Exception {
    mockMvc.perform(put("/post/{postId}", 1))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  public void cantAccessPostUpdateIfNotAuth() throws Exception {
    mockMvc.perform(get("/post/{postId}/update", 1))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  public void canReachPostCreateForm() throws Exception {
    mockMvc.perform(get("/topic/{topicId}/post/create", 1))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("post/create"))
        .andExpect(model().attribute("topicId", 1L))
        .andExpect(model().attributeExists("post"));
  }

  @Test
  @WithMockUser
  public void testValidPostCreation() throws Exception {
    doNothing().when(jmsTemplate).convertAndSend(anyString(), any(Post.class));
    mockMvc.perform(post("/topic/{topicId}/post", 1)
        .param("content", "Nouveau post"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/topic/1"));
  }

  @Test
  @WithMockUser
  public void testNotValidPostCreation() throws Exception {
    mockMvc.perform(post("/topic/{topicId}/post", 1)
        .param("content", ""))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("post/create"))
        .andExpect(model().attributeHasFieldErrors("post", "content"));
  }

  @Test
  @WithMockUser
  public void testNotValidPostUpdate() throws Exception {
    when(postService.exists(anyLong())).thenReturn(true);
    when(postService.getOne(anyLong())).thenReturn(new Post().setId(1L).setContent("Post"));
    mockMvc.perform(put("/post/{postId}", 1)
        .param("content", ""))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("post/update"))
        .andExpect(model().attributeHasFieldErrors("post", "content"));
  }

  @Test
  public void testValidPostUpdate() throws Exception {
    when(postService.exists(anyLong())).thenReturn(true);
    when(postService.getOne(anyLong())).thenReturn(new Post().setId(1L).setContent("Post"));
    mockMvc.perform(delete("/posteeeee/{postId}", 1)
        .param("content", "Mise à jour post"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/topic"));
  }

  @Test
  @WithMockUser
  public void redirectToErrorPageWhenTopicDoesntExist() throws Exception {
    when(postService.save(any(Post.class), anyLong())).thenThrow(TopicNotFoundException.class);

    mockMvc.perform(post("/topic/{topicId}/post", 1)
        .param("content", "Salut"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
