package fr.miage.sid.forum.controller;

import fr.miage.sid.forum.config.security.CurrentUser;
import fr.miage.sid.forum.config.security.MyPrincipal;
import fr.miage.sid.forum.domain.Post;
import fr.miage.sid.forum.domain.Topic;
import fr.miage.sid.forum.service.PostService;
import fr.miage.sid.forum.service.TopicService;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import fr.miage.sid.forum.service.UserService;
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
                         PostService postService, UserService userService) {
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
      String postContent,
      @PathVariable("projectId") Long projectId) {
    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors() || postContent.equals("")) {
      modelAndView.setViewName("topic/create");
      modelAndView.addObject("errorPostContent", postContent);
      return modelAndView.addObject("projectId", projectId);
    }

    Topic saved = topicService.save(topic, projectId);
    postService.save(new Post().setContent(postContent), saved.getId());
    modelAndView.setViewName("redirect:/project/" + projectId);

    return modelAndView;
  }

  @GetMapping("/topic/{topicId}")
  public ModelAndView showTopic(@PathVariable("topicId") Long topicId,
                                @CurrentUser MyPrincipal principal) {
    ModelAndView modelAndView = new ModelAndView();

    try {
      Topic topic = topicService.getOne(topicId);
      modelAndView.setViewName("topic/show");
      modelAndView.addObject("topic", topic);
      modelAndView.addObject("posts", postService.getAllByTopic(topic));
      if(principal != null){
        modelAndView.addObject("currentUser", userService.getOne(principal.getId()));
      }
    } catch (EntityNotFoundException e) {
      ViewUtils.setErrorView(modelAndView, HttpStatus.NOT_FOUND, "This topic doesn't exist");
    }

    return modelAndView;
  }
}
