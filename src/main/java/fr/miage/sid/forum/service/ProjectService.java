package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Project;
import java.util.List;

public interface ProjectService {
  Project save(Project project, Long userId);
  Project getOne(Long projectId);
  List<Project> getAll();
}
