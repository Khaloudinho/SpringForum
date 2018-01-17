package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.*;
import fr.miage.sid.forum.exception.TopicNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final TopicRepository topicRepository;

  @Autowired
  public PostServiceImpl(PostRepository postRepository,
      TopicRepository topicRepository) {
    this.postRepository = postRepository;
    this.topicRepository = topicRepository;
  }

  @Override
  public Post save(Post post, Long topicId) throws TopicNotFoundException {
    Topic topic = topicRepository.getOne(topicId);
    if (topic == null) {
      throw new TopicNotFoundException("Can't find topic with id: " + topicId);
    }
    post.setTopic(topic);
    return postRepository.save(post);
  }

  @Override
  public List<Post> getAllByTopic(Topic topic) {
    return postRepository.getAllByTopic(topic);
  }

  @Override
  public int countCreatedByUser(User user) {
    return postRepository.countAllByCreatedBy(user);
  }
}
