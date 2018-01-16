package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Topic;
import java.util.List;

public interface TopicService {

  Topic save(Topic topic);

  Topic save(Topic topic, Long projectId, Long userId);

  Topic getOne(Long id);

  List<Topic> getAllByProject(Project project);

  boolean exists(Long id);
}
