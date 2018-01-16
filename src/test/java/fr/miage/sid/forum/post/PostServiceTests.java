package fr.miage.sid.forum.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.MockitoAnnotations.initMocks;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.PostRepository;
import fr.miage.sid.forum.domain.TopicRepository;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.PostServiceImpl;
import fr.miage.sid.forum.exception.TopicNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PostServiceTests {

  private PostService postService;

  @Mock
  private PostRepository postRepository;

  @Mock
  private TopicRepository topicRepository;

  @Before
  public void setUp() {
    initMocks(this);
    this.postService = new PostServiceImpl(postRepository, topicRepository);
  }

  @Test
  public void createPostWithSuccess() {
    Topic postTopic = new Topic().setId(4L);
    Post expected = new Post().setTopic(postTopic);
    given(this.topicRepository.getOne(anyLong())).willReturn(postTopic);
    given(this.postRepository.save(new Post().setTopic(postTopic))).willReturn(expected);

    Post actual = postService.save(new Post(), postTopic.getId());
    assertThat(actual).isEqualTo(expected);
    assertThat(actual.getTopic()).isEqualTo(postTopic);
  }

  @Test(expected = TopicNotFoundException.class)
  public void inexistantTopicIdShouldThrow() {
    given(this.topicRepository.getOne(anyLong())).willReturn(null);
    this.postService.save(new Post(), 1L);
  }

  /**
   * Pas le plus intéressant, c'est un test de repository déguisé
   */
  @Test
  public void getAllByTopicReturnAllPosts() {
    Topic topic = new Topic().setId(1L);
    List<Post> posts = Arrays.asList(new Post().setTopic(topic), new Post());
    given(this.postRepository.getAllByTopic(topic)).willReturn(posts);

    List<Post> actual = postService.getAllByTopic(topic);
    assertThat(actual).hasSameSizeAs(posts);
    assertThat(actual.get(0)).isEqualToComparingOnlyGivenFields(posts.get(0), "id");
  }
}
