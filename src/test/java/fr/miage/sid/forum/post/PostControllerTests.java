package fr.miage.sid.forum.post;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import fr.miage.sid.forum.controller.PostController;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicNotFoundException;
import fr.miage.sid.forum.service.TopicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @MockBean
  private TopicService topicService;

  @Test
  public void cantAccessPostCreationIfNotAuth() throws Exception {
    mockMvc.perform(get("/topic/{topicId}/post/create", 1))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void cantCreatePostIfNotAuth() throws Exception {
    mockMvc.perform(post("/topic/{topicId}/post", 1))
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
  public void redirectToErrorPageWhenTopicDoesntExist() throws Exception {
    when(postService.save(any(Post.class), anyLong())).thenThrow(TopicNotFoundException.class);

    mockMvc.perform(post("/topic/{topicId}/post", 1)
        .param("content", "Salut"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
