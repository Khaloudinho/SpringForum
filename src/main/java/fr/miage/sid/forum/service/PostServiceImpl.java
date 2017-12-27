package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.repository.PostRepository;
import fr.miage.sid.forum.repository.TopicRepository;
import fr.miage.sid.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final TopicRepository topicRepository;
  private final UserRepository userRepository;

  @Autowired
  public PostServiceImpl(PostRepository postRepository,
      TopicRepository topicRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.topicRepository = topicRepository;
    this.userRepository = userRepository;
  }


  @Override
  public Post save(Post post) {
    return postRepository.save(post);
  }

  @Override
  public Post save(Post post, Long topicId, Long userId) {
    post.setUser(userRepository.getOne(userId));
    post.setTopic(topicRepository.getOne(topicId));
    return postRepository.save(post);
  }
}
