package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Permission;
import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.ProjectRepository;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.TopicRepository;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

  private final TopicRepository topicRepository;
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Autowired
  public TopicServiceImpl(TopicRepository topicRepository,
      ProjectRepository projectRepository,
      UserRepository userRepository) {
    this.topicRepository = topicRepository;
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
  }


  @Override
  public Topic save(Topic topic) {
    return topicRepository.save(topic);
  }

  @Override
  public Topic save(Topic topic, Long projectId, Long userId) {
    if (projectRepository.exists(projectId)) {
      User tmp = userRepository.findOne(userId);
      Project project = projectRepository.getOne(projectId);
      topic.givePermissionTo(tmp.getId(), Permission.ALL);
//      project.addTopic(topic);
      return topicRepository.save(topic);
    }
    return null;
  }

  @Override
  public Topic getOne(Long id) {
    return topicRepository.getOne(id);
  }

  @Override
  public List<Topic> getAllByProject(Project project) {
    return topicRepository.getAllByProject(project);
  }

  @Override
  public boolean exists(Long id) {
    return topicRepository.exists(id);
  }
}
