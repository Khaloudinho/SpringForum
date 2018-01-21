package fr.miage.sid.forum.load;

import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleLoadTests {

  @Autowired
  private PostService postService;

  @Autowired
  private TopicService topicService;

  @Test
  public void testLoadPostsCreation() {
    postService.loadTestCreation(1000);
  }

  @Test
  public void testLoadTopicCreation() {
    topicService.loadTestCreation(1000);
  }

}
