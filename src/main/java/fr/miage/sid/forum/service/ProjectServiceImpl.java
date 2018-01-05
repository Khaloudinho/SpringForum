package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.repository.ProjectRepository;
import fr.miage.sid.forum.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService{

  private ProjectRepository projectRepository;
  private UserRepository userRepository;

  @Autowired
  public ProjectServiceImpl(ProjectRepository projectRepository,
      UserRepository userRepository) {
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Project save(Project project, Long userId) {
    project.setCreator(userRepository.getOne(userId));
    return projectRepository.save(project);
  }

  @Override
  public Project getOne(Long projectId) {
    return projectRepository.getOne(projectId);
  }

  @Override
  public List<Project> getAll() {
    return projectRepository.findAll();
  }
}
