package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
import java.util.Set;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class ProjectController {

  private final ProjectService projectService;
  private final TopicService topicService;
  private final UserService userService;

  @Autowired
  public ProjectController(ProjectService projectService,
      TopicService topicService, UserService userService) {
    this.projectService = projectService;
    this.topicService = topicService;
    this.userService = userService;
  }

  /**
   * Projects are the highest containers of this forum, the index list them all
   */
  @GetMapping("/")
  public ModelAndView getAll() {
    ModelAndView modelAndView = new ModelAndView("project/index");
    modelAndView.addObject("projects", projectService.getAllAllowed());
    return modelAndView;
  }

  /**
   * Return the form to create a project
   */
  @GetMapping("/project/create")
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
  public ModelAndView getProjectCreateForm(Project project) {
    ModelAndView modelAndView = new ModelAndView("project/create");
    modelAndView.addObject(project);
    return modelAndView;
  }

  /**
   * PostMapping, verify the validity of the project before saving
   */
  @PostMapping("/project")
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
  public ModelAndView createProject(@Valid Project project, BindingResult result) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.setViewName("project/create");
      return modelAndView;
    }

    projectService.save(project);
    modelAndView.setViewName("redirect:/");

    return modelAndView;
  }

  /**
   * Returns the list of topic of a project
   */
  @GetMapping("project/{projectId}")
  @PreAuthorize("@permissionService.canReadProject(#projectId)")
  public ModelAndView showProject(@PathVariable("projectId") Long projectId) {
    ModelAndView modelAndView = new ModelAndView();

    Project project = projectService.getOne(projectId);

    modelAndView.setViewName("project/show");
    modelAndView.addObject("project", project);
    modelAndView.addObject("topics", topicService.getAllByProject(project));
    return modelAndView;
  }

  /**
   * Return the form used to edit a project
   */
  @GetMapping("project/{projectId}/edit")
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
  public ModelAndView editProject(@PathVariable("projectId") Long projectId) {
    ModelAndView modelAndView = new ModelAndView();

    Project project = projectService.getOne(projectId);
    Set<User> readers = userService.getAllProjectReaders(projectId);
    Set<User> writers = userService.getAllProjectWriters(projectId);

    modelAndView.setViewName("project/edit");
    modelAndView.addObject("project", project);
    modelAndView.addObject("users", userService.getAll());
    modelAndView.addObject("usersReader", readers);
    modelAndView.addObject("usersWriter", writers);

    return modelAndView;
  }


  /**
   * Put handler to edit a project's name and anonymousAccess
   */
  @PutMapping("project/{projectId}")
  @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
  public ModelAndView editProjectName(@PathVariable("projectId") Long projectId, String name,
      boolean anonymousCanAccess) {
    ModelAndView modelAndView = new ModelAndView();

    Project project = projectService.getOne(projectId).setName(name);
    project.setAnonymousCanAccess(anonymousCanAccess);
    Project saved = projectService.save(project);

    modelAndView.addObject("project", saved);
    modelAndView.addObject("users", userService.getAll());
    modelAndView.setViewName("redirect:/");

    return modelAndView;
  }
}
