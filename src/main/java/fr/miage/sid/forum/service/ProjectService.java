package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.User;

import java.util.List;

public interface ProjectService {

  Project save(Project project);

  Project getOne(Long projectId);

  List<Project> getAll();

  int countCreatedByUser(User user);
}
