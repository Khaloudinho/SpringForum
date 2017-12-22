package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.repository.ProjectRepository;
import fr.miage.sid.forum.repository.TopicRepository;
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
  public Topic save(Topic topic) {
    return topicRepository.save(topic);
  }

  @Override
  public Topic save(Topic topic, String projectId) {
    topic.setProject(projectRepository.getOne(Long.valueOf(projectId)));
    return topicRepository.save(topic);
  }
}
