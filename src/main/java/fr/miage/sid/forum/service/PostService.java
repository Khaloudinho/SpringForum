package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.exception.PermissionPostException;
import java.util.List;

public interface PostService {
  Post save(Post post);
  Post save(Post post, Long topicId, Long userId) throws PermissionPostException;
  List<Post> getAllByTopic(Topic topic);
}
