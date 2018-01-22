package fr.miage.sid.forum.post;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import fr.miage.sid.forum.config.security.MethodSecurityConfig;
import fr.miage.sid.forum.config.security.PermissionService;
import fr.miage.sid.forum.config.security.WebSecurityConfig;
import fr.miage.sid.forum.config.security.WithMockMyPrincipal;
import fr.miage.sid.forum.controller.PostController;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
@Import({WebSecurityConfig.class, MethodSecurityConfig.class})
public class PostControllerTests {

  private static final String LOGIN_URL = "http://localhost/login";

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
  private PermissionService permissionService;

  @MockBean
  private JmsTemplate jmsTemplate;

  @Test
  @WithAnonymousUser
  public void cantAccessPostCreationIfNotAuth() throws Exception {
    mockMvc.perform(get("/topic/{topicId}/post/create", 1))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LOGIN_URL));
  }

  @Test
  @WithAnonymousUser
  public void cantCreatePostIfNotAuth() throws Exception {
    mockMvc.perform(post("/topic/{topicId}/post", 1))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithAnonymousUser
  public void cantUpdatePostIfNotAuth() throws Exception {
    mockMvc.perform(put("/post/{postId}", 1))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithAnonymousUser
  public void cantAccessPostUpdateIfNotAuth() throws Exception {
    mockMvc.perform(get("/post/{postId}/update", 1))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LOGIN_URL));
  }

  @Test
  @WithMockMyPrincipal
  public void canReachPostCreateForm() throws Exception {
    // Given that user can write in topic
    when(permissionService.canWriteTopic(anyLong())).thenReturn(true);

    mockMvc.perform(get("/topic/{topicId}/post/create", 1))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("post/create"))
        .andExpect(model().attribute("topicId", 1L))
        .andExpect(model().attributeExists("post"));
  }

  @Test
  @WithMockMyPrincipal(roles = {"ROLE_ADMIN"})
  public void canReachPostUpdateFormIfAdmin() throws Exception {
    // Given that user can write in topic
    when(permissionService.canWriteTopic(anyLong())).thenReturn(true);
    when(postService.getOne(anyLong())).thenReturn(new Post().setId(1L).setContent("Post"));

    mockMvc.perform(get("/post/{postId}/update", 1))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("post/update"))
        .andExpect(model().attributeExists("currentPost"));
  }

  @Test
  @WithMockMyPrincipal
  public void canReachPostUpdateFormIfCreator() throws Exception {
    Post post = new Post().setId(1L).setContent("Post");

    // Given that user can write in topic
    when(permissionService.canWriteTopic(anyLong())).thenReturn(true);
    when(postService.getOne(anyLong())).thenReturn(post);
    when(postService.isCreator(anyLong(), any(Post.class))).thenReturn(true);

    mockMvc.perform(get("/post/{postId}/update", 1))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("post/update"))
        .andExpect(model().attributeExists("currentPost"))
        .andExpect(model().attribute("currentPost", hasProperty("content", is("Post"))));
  }

  @Test
  @WithMockMyPrincipal
  public void testValidPostCreation() throws Exception {
    // Given that user can write in topic
    when(permissionService.canWriteTopic(anyLong())).thenReturn(true);
    // Don't send emails
    doNothing().when(jmsTemplate).convertAndSend(anyString(), any(Post.class));

    mockMvc.perform(post("/topic/{topicId}/post", 1).with(csrf())
        .param("content", "Nouveau post"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/topic/1"));
  }

  @Test
  @WithMockMyPrincipal
  public void testNotValidPostCreation() throws Exception {
    // Given that user can write in topic
    when(permissionService.canWriteTopic(anyLong())).thenReturn(true);
    mockMvc.perform(post("/topic/{topicId}/post", 1).with(csrf())
        .param("content", ""))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("post/create"))
        .andExpect(model().attributeHasFieldErrors("post", "content"));
  }

  @Test
  @WithMockMyPrincipal(roles = {"ROLE_ADMIN"})
  public void testNotValidPostUpdate() throws Exception {
    when(permissionService.canWriteTopic(anyLong())).thenReturn(true);
    when(postService.getOne(anyLong())).thenReturn(new Post().setId(1L).setContent("Post"));
    mockMvc.perform(put("/post/{postId}", 1).with(csrf())
        .param("content", ""))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("post/update"))
        .andExpect(model().attributeHasFieldErrors("post", "content"));
  }

  @Test
  @WithMockMyPrincipal(roles = {"ROLE_ADMIN"})
  public void testValidPostUpdate() throws Exception {

    when(permissionService.canWriteTopic(anyLong())).thenReturn(true);
    when(postService.getOne(anyLong())).thenReturn(new Post().setId(1L).setContent("Post")
        .setTopic(new Topic().setId(1L)));

    mockMvc.perform(put("/post/{postId}", 1).with(csrf())
        .param("content", "Mise à jour post"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/topic/1"));
  }

  @Test
  @WithMockMyPrincipal
  public void redirectToErrorPageWhenTopicDoesntExist() throws Exception {
    when(permissionService.canWriteTopic(anyLong())).thenReturn(true);
    when(postService.save(any(Post.class), anyLong())).thenThrow(TopicNotFoundException.class);

    mockMvc.perform(post("/topic/{topicId}/post", 1).with(csrf())
        .param("content", "Salut"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
