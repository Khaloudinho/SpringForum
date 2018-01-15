package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import java.util.List;

public interface PostService {

  Post save(Post post, Long topicId, Long userId);

  List<Post> getAllByTopic(Topic topic);
}
