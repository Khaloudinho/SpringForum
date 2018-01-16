package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
  List<Topic> getAllByProject(Project project);
}
