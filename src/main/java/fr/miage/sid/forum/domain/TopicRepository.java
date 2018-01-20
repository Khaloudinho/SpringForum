package fr.miage.sid.forum.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

  List<Topic> getAllByProject(Project project);

  @Query("select t from Topic t left join FETCH t.followers f where t.id  = ?1")
  Topic eagerWithFollowers(Long id);

  int countAllByCreatedBy(User user);
}
