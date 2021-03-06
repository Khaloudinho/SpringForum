package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.exception.ProjectNotFoundException;
import java.util.List;

public interface TopicService {

  Topic save(Topic topic);

  Topic save(Topic topic, Long projectId) throws ProjectNotFoundException;

  Topic getOne(Long id);

  List<Topic> getAllByProject(Project project);

  int countCreatedByUser(User user);

  boolean isCreator(Long userId, Topic topic);

  boolean isFollowing(Long userId, Topic topic);

  void loadTestCreation(int maxCreate);
}
