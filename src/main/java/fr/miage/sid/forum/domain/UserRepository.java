package fr.miage.sid.forum.domain;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  User findByEmail(String email);

  @Query("select u from User u left join FETCH u.roles r where u.oauthId = ?1 and password is null")
  User eagerFindByOauthId(String oauthId);

  @Query("select u from User u left join FETCH u.roles r where u.email = ?1 and password is not null")
  User eagerFindByEmail(String email);

  @Query(value = "SELECT users.* FROM users INNER JOIN project_readers ON users.id = project_readers.readers WHERE project_readers.project_id = ?1"
      , nativeQuery = true)
  Set<User> getAllProjectReaders(Long projectId);

  @Query(value = "SELECT users.* FROM users INNER JOIN project_writers ON users.id = project_writers.writers WHERE project_writers.project_id = ?1"
      , nativeQuery = true)
  Set<User> getAllProjectWriters(Long projectId);

  @Query(value = "SELECT users.* FROM users INNER JOIN topic_readers ON users.id = topic_readers.readers WHERE topic_readers.topic_id = ?1"
      , nativeQuery = true)
  Set<User> getAllTopicReaders(Long topicId);

  @Query(value = "SELECT users.* FROM users INNER JOIN topic_writers ON users.id = topic_writers.writers WHERE topic_writers.topic_id = ?1"
      , nativeQuery = true)
  Set<User> getAllTopicWriters(Long topicId);
}
