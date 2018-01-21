package fr.miage.sid.forum.service;

import fr.miage.sid.forum.config.security.UserDetailsImpl;
import fr.miage.sid.forum.domain.User;
import java.util.List;
import java.util.Set;

public interface UserService {

  User save(User user);

  User getOne(Long id);

  List<User> getAll();

  UserDetailsImpl getUserDetails(User user);

  User eagerFindByEmail(String email);

  Set<User> getAllProjectReaders(Long projectId);

  Set<User> getAllProjectWriters(Long projectId);

  Set<User> getAllTopicReaders(Long topicId);

  Set<User> getAllTopicWriters(Long topicId);
}
