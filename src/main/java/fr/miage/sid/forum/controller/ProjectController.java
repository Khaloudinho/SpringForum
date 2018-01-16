package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.TopicService;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProjectController {

  private final ProjectService projectService;
  private final TopicService topicService;

  @Autowired
  public ProjectController(ProjectService projectService,
      TopicService topicService) {
    this.projectService = projectService;
    this.topicService = topicService;
  }

  @GetMapping("project")
  public ModelAndView getAll() {
    ModelAndView modelAndView = new ModelAndView("project/home");
    modelAndView.addObject("projects", projectService.getAll());
    return modelAndView;
  }

  @GetMapping("project/new")
  public ModelAndView getTopicForm(Project project) {
    ModelAndView modelAndView = new ModelAndView("project/new");
    modelAndView.addObject(project);
    return modelAndView;
  }

  @PostMapping("project/new")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView createTopic(@Valid Project project, BindingResult result,
      @CurrentUser MyPrincipal principal) {

    ModelAndView modelAndView = new ModelAndView();
    if (result.hasErrors()) {
      modelAndView.setViewName("project/new");
    } else {
      projectService.save(project, principal.getId());
      modelAndView.setViewName("redirect:/");
    }
    return modelAndView;
  }

  @GetMapping("project/{projectId}")
  public ModelAndView getOne(@PathVariable("projectId") String projectId) {
    ModelAndView modelAndView = new ModelAndView();

    try {
      Project project = projectService.getOne(Long.valueOf(projectId));
      modelAndView.setViewName("project/projectPage");
      modelAndView.addObject("project", project);
      modelAndView.addObject("topics", topicService.getAllByProject(project));
    } catch (NumberFormatException | EntityNotFoundException e) {
      modelAndView.setViewName("error/basicTemplate");
      modelAndView.setStatus(HttpStatus.NOT_FOUND);
      modelAndView.addObject("errorCode", "404 Not Found");
      modelAndView.addObject("message", "This project does not exist");
    }

    return modelAndView;
  }

  @GetMapping("project/{projectId}/editaccess")
  public ModelAndView editAccess(@PathVariable("projectId") String projectId) {
    ModelAndView modelAndView = new ModelAndView();

    try {
      Project project = projectService.getOne(Long.valueOf(projectId));
      modelAndView.setViewName("project/editAccessPage");
      modelAndView.addObject("project", project);
    } catch (NumberFormatException | EntityNotFoundException e) {
      modelAndView.setViewName("error/basicTemplate");
      modelAndView.setStatus(HttpStatus.NOT_FOUND);
      modelAndView.addObject("errorCode", "404 Not Found");
      modelAndView.addObject("message", "This project does not exist");
    }

    return modelAndView;
  }


}
