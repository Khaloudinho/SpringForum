package fr.miage.sid.forum.service;

import fr.miage.sid.forum.config.security.UserDetailsImpl;
import fr.miage.sid.forum.domain.User;
import java.util.List;

public interface UserService {

  User save(User user);

  User getOne(Long id);

  List<User> getAll();

  UserDetailsImpl getUserDetails(User user);

  User eagerFindByEmail(String email);
}
