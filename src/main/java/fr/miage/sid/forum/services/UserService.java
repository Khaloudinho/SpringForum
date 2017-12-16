package fr.miage.sid.forum.services;

import fr.miage.sid.forum.domain.User;

public interface UserService {

  User findUserByEmail(String email);

  void save(User user);
}
