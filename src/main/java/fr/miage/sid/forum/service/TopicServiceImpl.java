package fr.miage.sid.forum.service;

import fr.miage.sid.forum.aspects.LogExecutionTime;
import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.ProjectRepository;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.TopicRepository;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserRepository;
import fr.miage.sid.forum.exception.ProjectNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

  private final TopicRepository topicRepository;
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Autowired
  public TopicServiceImpl(TopicRepository topicRepository,
      ProjectRepository projectRepository, UserRepository userRepository) {
    this.topicRepository = topicRepository;
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Topic save(Topic topic) {
    return topicRepository.save(topic);
  }

  @Override
  public Topic save(Topic topic, Long projectId) throws ProjectNotFoundException {
    Project project = projectRepository.getOne(projectId);

    if (project == null) {
      throw new ProjectNotFoundException("Can't find project with id: " + projectId);
    }

    topic.setProject(project);

//    topic.givePermissionToAll(project.getReaders(), Permission.READ);
//    topic.givePermissionToAll(project.getWriters(), Permission.WRITE);
//    topic.setAnonymousCanAccess(project.isAnonymousCanAccess());

    return topicRepository.save(topic);
  }

  @Override
  public Topic getOne(Long id) {
    return topicRepository.getOne(id);
  }

  @Override
  @PostFilter("@permissionService.canReadTopic(filterObject.id)")
  public List<Topic> getAllByProject(Project project) {
    return topicRepository.getAllByProject(project);
  }

  @Override
  public int countCreatedByUser(User user) {
    return topicRepository.countAllByCreatedBy(user);
  }

  @Override
  public boolean isFollowing(Long userId, Topic topic) {
    User user = userRepository.getOne(userId);
    return topic.getFollowers().contains(user);
  }

  @Override
  @LogExecutionTime
  public void loadTestCreation(int maxCreate) {
    Project project = projectRepository.save(new Project().setName("Bonjour"));
    for (int i = 0; i < maxCreate; i++) {
      save(new Topic().setTitle("Hello"), project.getId());
    }
  }
}
