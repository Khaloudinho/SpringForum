package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.exception.PermissionTopicException;

public interface TopicService {

  Topic save(Topic topic);
  Topic save(Topic topic, Long projectId, Long userId) throws PermissionTopicException;
  Topic getOne(Long id);

}
