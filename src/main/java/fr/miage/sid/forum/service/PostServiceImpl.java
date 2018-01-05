package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.exception.PermissionPostException;
import fr.miage.sid.forum.repository.PostRepository;
import fr.miage.sid.forum.repository.TopicRepository;
import fr.miage.sid.forum.repository.UserRepository;
import java.util.List;
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
  public Post save(Post post, Long topicId, Long userId) throws PermissionPostException{

    if(topicRepository.exists(topicId)){
      Topic topic = topicRepository.getOne(topicId);
      post.setUser(userRepository.getOne(userId));
      topic.addPost(post);
//      topicRepository.save(topic);
      return postRepository.save(post);
    }

    return null;
  }

  @Override
  public List<Post> getAllByTopic(Topic topic) {
    return postRepository.getAllByTopic(topic);
  }
}
