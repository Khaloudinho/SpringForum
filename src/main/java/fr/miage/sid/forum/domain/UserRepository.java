package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  User findByEmail(String email);

  User findByOauthId(String oauthId);

  @Query("select u from User u left join FETCH u.roles r where u.email = ?1 and password is not null")
  User eagerFindByEmail(String email);

}