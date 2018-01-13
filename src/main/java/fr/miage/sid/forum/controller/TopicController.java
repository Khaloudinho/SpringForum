package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.exception.PermissionTopicException;
import fr.miage.sid.forum.security.CurrentUser;
import fr.miage.sid.forum.security.MyPrincipal;
import fr.miage.sid.forum.service.PostService;
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
public class TopicController {

  private final TopicService topicService;
  private final PostService postService;

  @Autowired
  public TopicController(TopicService topicService,
      PostService postService) {
    this.topicService = topicService;
    this.postService = postService;
  }

  @GetMapping("project/{projectId}/newtopic")
  public ModelAndView getTopicForm(Topic topic, @PathVariable("projectId") String projectId) {
    ModelAndView modelAndView = new ModelAndView("topic/new");
    modelAndView.addObject(topic);
    modelAndView.addObject("projectId", projectId);
    return modelAndView;
  }

  @PostMapping("project/{projectId}/newtopic")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView createTopic(
      @Valid Topic topic,
      BindingResult result,
      @PathVariable("projectId") String projectId,
      @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.setViewName("topic/new");
      modelAndView.addObject("projectId", projectId);
    } else {
      try {
        Topic createdTopic = topicService.save(topic, Long.valueOf(projectId), principal.getId());
        modelAndView.setViewName("redirect:/");
        if (createdTopic == null) {
          modelAndView.setViewName("error/basicTemplate");
          modelAndView.setStatus(HttpStatus.NOT_FOUND);
          modelAndView.addObject("errorCode", "404 Not Found");
          modelAndView.addObject("message",
              "This project does not exist, making a new topic is impossible");
        }
      } catch (PermissionTopicException e) {
        modelAndView.setViewName("error/basicTemplate");
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        modelAndView.addObject("errorCode", "403 Forbidden");
        modelAndView
            .addObject("message", "You do not have the permission to make a topic on this project");
        return modelAndView;
      }
    }
    return modelAndView;
  }

  @GetMapping("/topic/{topicId}")
  public ModelAndView getOne(@PathVariable("topicId") String topicId) {
    ModelAndView modelAndView = new ModelAndView();

    try {
      Topic topic = topicService.getOne(Long.valueOf(topicId));
      modelAndView.setViewName("topic/topicPage");
      modelAndView.addObject("topic", topic);
      modelAndView.addObject("posts", postService.getAllByTopic(topic));
    } catch (NumberFormatException | EntityNotFoundException e) {
      modelAndView.setViewName("error/basicTemplate");
      modelAndView.setStatus(HttpStatus.NOT_FOUND);
      modelAndView.addObject("errorCode", "404 Not Found");
      modelAndView.addObject("message", "This topic does not exist");
    }

    return modelAndView;
  }
}
