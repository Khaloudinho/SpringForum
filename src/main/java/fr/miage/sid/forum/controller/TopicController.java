package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.exception.PermissionTopicException;
import fr.miage.sid.forum.repository.ProjectRepository;
import fr.miage.sid.forum.repository.TopicRepository;
import fr.miage.sid.forum.security.MyPrincipal;
import fr.miage.sid.forum.service.TopicService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TopicController {

  private final TopicService topicService;

  @Autowired
  public TopicController(TopicService topicService) {
    this.topicService = topicService;
  }

  @GetMapping("project/{projectId}/newtopic")
  public ModelAndView getTopicForm(Topic topic, @PathVariable("projectId") String projectId){
    ModelAndView modelAndView = new ModelAndView("topic/new");
    modelAndView.addObject(topic);
    modelAndView.addObject("projectId", projectId);
    return modelAndView;
  }

  @PostMapping("project/{projectId}/newtopic")
  public ModelAndView createTopic(
      @Valid Topic topic,
      BindingResult result,
      @PathVariable("projectId") String projectId,
      @AuthenticationPrincipal MyPrincipal principal){
    ModelAndView modelAndView = new ModelAndView();

    if(principal == null){// user is somehow anonymous
      modelAndView.setViewName("error/basicTemplate");
      modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
      modelAndView.addObject("errorCode", "401 Unauthorized");
      modelAndView.addObject("message", "You did not provide any HTTP authentication");
      return modelAndView;
    }

    if(result.hasErrors()){
      modelAndView.setViewName("topic/new");
      modelAndView.addObject("projectId", projectId);
    } else {
      try {
        Topic createdTopic = topicService.save(topic, Long.valueOf(projectId), principal.getId());
        modelAndView.setViewName("redirect:/");
        if(createdTopic == null){
          modelAndView.setViewName("error/basicTemplate");
          modelAndView.setStatus(HttpStatus.NOT_FOUND);
          modelAndView.addObject("errorCode", "404 Not Found");
          modelAndView.addObject("message", "This project does not exist, making a new topic is impossible");
        }
      } catch (PermissionTopicException e){
        modelAndView.setViewName("error/basicTemplate");
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        modelAndView.addObject("errorCode", "403 Forbidden");
        modelAndView.addObject("message", "You do not have the permission to make a topic on this project");
        return modelAndView;
      }
    }
    return modelAndView;
  }


}
