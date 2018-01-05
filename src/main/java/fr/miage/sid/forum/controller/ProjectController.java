package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.repository.ProjectRepository;
import fr.miage.sid.forum.security.MyPrincipal;
import fr.miage.sid.forum.service.ProjectService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProjectController {

  private final ProjectService projectService;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @GetMapping("project")
  public ModelAndView getAll(){
    ModelAndView modelAndView = new ModelAndView("project/home");
    return modelAndView;
  }

  @GetMapping("project/new")
  public ModelAndView getTopicForm(Project project){
    ModelAndView modelAndView = new ModelAndView("project/new");
    modelAndView.addObject(project);
    return modelAndView;
  }

  @PostMapping("project/new")
  public ModelAndView createTopic(@Valid Project project, BindingResult result, @AuthenticationPrincipal MyPrincipal principal){
    ModelAndView modelAndView = new ModelAndView();

    if(principal == null){// user is somehow anonymous
      modelAndView.setViewName("error/basicTemplate");
      modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
      modelAndView.addObject("errorCode", "401 Unauthorized");
      modelAndView.addObject("message", "You did not provide any HTTP authentication");
      return modelAndView;
    }

    if(result.hasErrors()){
      modelAndView.setViewName("project/new");
    } else {
      projectService.save(project, principal.getId());
      modelAndView.setViewName("redirect:/");
    }
    return modelAndView;
  }

}
