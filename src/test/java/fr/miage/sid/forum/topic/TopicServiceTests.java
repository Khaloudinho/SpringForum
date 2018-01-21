package fr.miage.sid.forum.topic;

import fr.miage.sid.forum.domain.*;
import fr.miage.sid.forum.exception.ProjectNotFoundException;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.TopicServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
public class TopicServiceTests {

    private TopicService topicService;

    @Mock
    private TopicRepository topicRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        initMocks(this);
        this.topicService = new TopicServiceImpl(topicRepository, projectRepository, userRepository);
    }

    @Test
    public void createTopicWithSuccess(){
        Topic expected = new Topic().setId(1L);
        given(this.topicService.save(new Topic().setId(1L))).willReturn(expected);

        Topic actual = this.topicService.save(new Topic().setId(1L));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void inexistantTopicIsNull() {
        assertThat(this.topicService.getOne(anyLong())).isEqualTo(null);
    }

    @Test(expected = ProjectNotFoundException.class)
    public void inexistantProjectIdShouldThrow() {
        given(this.projectRepository.getOne(anyLong())).willReturn(null);
        this.topicService.save(new Topic(), 1L);
    }

    @Test
    public void getOneTest() {
        Topic expected = new Topic().setId(1L);
        given(this.topicRepository.getOne(1L)).willReturn(expected);

        Topic actual = topicService.getOne(1L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllByProjectTest() {
        Project project = new Project().setId(1L);
        List<Topic> topics = Arrays.asList(new Topic().setProject(project));
        given(this.topicRepository.getAllByProject(project)).willReturn(topics);
        List<Topic> actual = topicService.getAllByProject(project);

        assertThat(actual).hasSameSizeAs(topics);
        assertThat(actual.get(0)).isEqualTo(topics.get(0));
    }

    @Test
    public void countProjectsCreatedByUserTest() {
        given(topicRepository.countAllByCreatedBy(any(User.class))).willReturn(3);

        assertThat(topicService.countCreatedByUser(new User())).isEqualTo(3);
    }

    @Test
    public void isCreatorTest() {
        Topic topic = new Topic();
        topic.setCreatedBy(new User().setId(1L));

        assertThat(topicService.isCreator(1L, topic));
    }

    @Test
    public void isFollowingTest() {
        Topic topic = new Topic();
        User user = new User().setId(1L);
        topic.addFollower(user);
        given(userRepository.getOne(1L)).willReturn(user);

        assertThat(topicService.isFollowing(1L, topic));
    }

}
