package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.User;

public interface UserService {

  User findUserByEmail(String email);

  User save(User user);
}
