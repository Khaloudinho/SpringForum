package fr.miage.sid.forum.service;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.ProjectRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

  private ProjectRepository projectRepository;

  @Autowired
  public ProjectServiceImpl(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Override
  public Project save(Project project) {
    // TODO Implement giving Permissions. Permissions on project can only come from admin
//    project.givePermissionTo(userRepository.showTopic(userId).getId(), Permission.ALL);
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
