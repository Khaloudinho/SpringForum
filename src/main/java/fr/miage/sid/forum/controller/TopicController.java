package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.domain.User;
import fr.miage.sid.forum.domain.UserRepository;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicService;
import fr.miage.sid.forum.service.UserService;
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
  private final UserService userService;

  @Autowired
  public TopicController(TopicService topicService,
      PostService postService,UserService userService) {
    this.topicService = topicService;
    this.postService = postService;
    this.userService = userService;
  }

  @GetMapping("project/{projectId}/topic/create")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView getTopicCreateForm(Topic topic, @PathVariable("projectId") String projectId) {
    ModelAndView modelAndView = new ModelAndView("topic/create");
    modelAndView.addObject(topic);
    modelAndView.addObject("projectId", projectId);
    return modelAndView;
  }

  @PostMapping("project/{projectId}/topic")
  @PreAuthorize("isAuthenticated()")
  public ModelAndView createTopic(
      @Valid Topic topic,
      BindingResult result,
      @PathVariable("projectId") String projectId,
      @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.setViewName("topic/create");
      modelAndView.addObject("projectId", projectId);
    }
    topic.addFollower(userService.getOne(principal.getId()));
    Topic createdTopic = topicService.save(topic, Long.valueOf(projectId), principal.getId());
    modelAndView.setViewName("redirect:/");
    if (createdTopic == null) {
      ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND,
          "This project does not exist, making a new topic is impossible");
    }

    return modelAndView;
  }

  @GetMapping("/topic/{topicId}")
  public ModelAndView getOne(@PathVariable("topicId") Long topicId) {
    ModelAndView modelAndView = new ModelAndView();

    try {
      Topic topic = topicService.getOne(topicId);
      modelAndView.setViewName("topic/show");
      modelAndView.addObject("topic", topic);
      modelAndView.addObject("posts", postService.getAllByTopic(topic));
    } catch (EntityNotFoundException e) {
      ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "This topic doesn't exist");
    }

    return modelAndView;
  }
}
