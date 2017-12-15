package fr.miage.sid.forum.repository;

import fr.miage.sid.forum.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  User findByUsername(String username);
}
