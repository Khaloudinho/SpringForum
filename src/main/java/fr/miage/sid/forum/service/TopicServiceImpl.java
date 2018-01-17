package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.*;
import fr.miage.sid.forum.exception.ProjectNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

  private final TopicRepository topicRepository;
  private final ProjectRepository projectRepository;

  @Autowired
  public TopicServiceImpl(TopicRepository topicRepository,
      ProjectRepository projectRepository) {
    this.topicRepository = topicRepository;
    this.projectRepository = projectRepository;
  }

  @Override
  public Topic save(Topic topic, Long projectId) throws ProjectNotFoundException {
    Project project = projectRepository.getOne(projectId);

    if (project == null) {
      throw new ProjectNotFoundException("Can't find project with id: " + projectId);
    }

    topic.setProject(project);

    topic.givePermissionToAll(project.getReaders(), Permission.READ);
    topic.givePermissionToAll(project.getWriters(), Permission.WRITE);
    topic.setAnonymousCanAccess(project.isAnonymousCanAccess());

    return topicRepository.save(topic);
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
  public int countCreatedByUser(User user) {
    return topicRepository.countAllByCreatedBy(user);
  }
}
