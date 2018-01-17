package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.User;

import java.util.List;
import java.util.Set;

public interface UserService {

  User findUserByEmail(String email);

  User save(User user);

  User getOne(Long id);

  List<User> getAll();
}
