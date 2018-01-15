package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.exception.PermissionTopicException;
import java.util.List;

public interface TopicService {

  Topic save(Topic topic);

  Topic save(Topic topic, Long projectId, Long userId) throws PermissionTopicException;

  Topic getOne(Long id);

  List<Topic> getAllByProject(Project project);

  boolean exists(Long id);
}
