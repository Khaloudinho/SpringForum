package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Topic;

public interface TopicService {

  Topic save(Topic topic);
  Topic save(Topic topic, String projectId);

}
