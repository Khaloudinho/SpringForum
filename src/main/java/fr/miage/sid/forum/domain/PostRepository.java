package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> getAllByTopic(Topic topic);
}
