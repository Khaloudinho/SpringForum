package fr.miage.sid.forum.service;

import fr.miage.sid.forum.aspects.LogExecutionTime;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.PostRepository;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.TopicRepository;
import fr.miage.sid.forum.domain.User;
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
  public Post save(Post post) {
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

  @Override
  public Post getOne(Long id) {
    return postRepository.getOne(id);
  }

  @Override
  public boolean exists(Long id) {
    return postRepository.exists(id);
  }

  @Override
  public boolean isCreator(Long userId, Post post) {
    return post.getCreatedBy().getId().equals(userId);
  }

  @Override
  @LogExecutionTime
  public void loadTestCreation(int maxCreate) {
    Topic topic = topicRepository.save(new Topic().setTitle("Bonjour"));
    for (int i = 0; i < maxCreate; i++) {
      save(new Post().setContent("Bonjour").setTopic(topic), 1L);
    }
  }
}
