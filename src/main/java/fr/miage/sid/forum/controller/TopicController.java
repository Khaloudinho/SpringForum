package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.domain.Topic;
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
      @PathVariable("projectId") Long projectId) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
      modelAndView.setViewName("topic/create");
      return modelAndView.addObject("projectId", projectId);
    }

    topicService.save(topic, projectId);
    modelAndView.setViewName("redirect:/project/" + projectId);

    return modelAndView;
  }

  @GetMapping("/topic/{topicId}")
  public ModelAndView showTopic(@PathVariable("topicId") Long topicId) {
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
