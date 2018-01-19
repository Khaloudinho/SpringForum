package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.exception.TopicNotFoundException;
import java.util.List;

public interface PostService {

  Post save(Post post, Long topicId) throws TopicNotFoundException;

  Post save(Post post);

  List<Post> getAllByTopic(Topic topic);

  int countCreatedByUser(User user);

  Post getOne(Long id);

  boolean exists(Long id);

  boolean isCreator(Long userId, Post post);
}
